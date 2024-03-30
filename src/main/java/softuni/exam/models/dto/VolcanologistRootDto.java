package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "volcanologists")
@XmlAccessorType(XmlAccessType.FIELD)
public class VolcanologistRootDto implements Serializable {
@XmlElement(name = "volcanologist")
    private List<VolcanologistDto> volcanologistDtos;

    public List<VolcanologistDto> getVolcanologistDtos() {
        return volcanologistDtos;
    }

    public void setVolcanologistDtos(List<VolcanologistDto> volcanologistDtos) {
        this.volcanologistDtos = volcanologistDtos;
    }
}
