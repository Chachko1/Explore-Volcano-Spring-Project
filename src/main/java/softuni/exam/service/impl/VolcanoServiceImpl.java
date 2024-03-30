package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountryDto;
import softuni.exam.models.dto.VolcanoDto;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.Volcano;
import softuni.exam.models.enums.VolcanoType;
import softuni.exam.repository.CountryRepository;
import softuni.exam.repository.VolcanoRepository;
import softuni.exam.service.VolcanoService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolcanoServiceImpl implements VolcanoService {
    private final VolcanoRepository volcanoRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;
    private final CountryRepository countryRepository;

    private static final String FILE_PATH="src/main/resources/files/json/volcanoes.json";

    public VolcanoServiceImpl(VolcanoRepository volcanoRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson, CountryRepository countryRepository) {
        this.volcanoRepository = volcanoRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
        this.countryRepository = countryRepository;
    }


    @Override
    public boolean areImported() {
        return this.volcanoRepository.count()>0;
    }

    @Override
    public String readVolcanoesFileContent() throws IOException {
        return new String(Files.readAllBytes(Path.of(FILE_PATH)));
    }

    @Override
    public String importVolcanoes() throws IOException {
        StringBuilder sb=new StringBuilder();

        VolcanoDto[] volcanoDtos=this.gson.fromJson(readVolcanoesFileContent(),VolcanoDto[].class);
        for (VolcanoDto volcanoDto:volcanoDtos){
            Optional<Volcano> optional=this.volcanoRepository.findByName(volcanoDto.getName());
            if (!validationUtil.isValid(volcanoDto)||optional.isPresent()){
                sb.append("Invalid volcano\n");
                continue;
            }
            Volcano volcano=this.modelMapper.map(volcanoDto,Volcano.class);
            volcano.setVolcanoType(VolcanoType.valueOf(volcanoDto.getVolcanoType()));
            volcano.setCountry(this.countryRepository.getById(volcanoDto.getCountry()));

            this.volcanoRepository.saveAndFlush(volcano);
            sb.append(String.format("Successfully imported volcano %s of type %s\n",volcano.getName(),volcano.getVolcanoType()));


        }
        return sb.toString();
    }

    @Override
    public String exportVolcanoes() {
        return this.volcanoRepository
                .findAllWhereElevationIsBiggerThan3000()
                .stream()
                .map(v -> String.format("Volcano: %s\n" +
                                "   *Located in: %s\n" +
                                "   **Elevation: %d\n" +
                                "   ***Last eruption on: %s\n",
                        v.getName(), v.getCountry().getName(), v.getElevation(), v.getLastEruption()))
                .collect(Collectors.joining());
    }
}