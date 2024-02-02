package nl.bsoft.apidemo.synchroniseren;

import lombok.Getter;
import lombok.Setter;
import nl.bsoft.apidemo.library.model.dto.BestuurlijkGebiedDto;
import nl.bsoft.apidemo.library.model.dto.LocatieDto;

@Getter
@Setter
public class SynchBestuurlijkgebied {
    private BestuurlijkGebiedDto bestuurlijkGebiedDto;
    private LocatieDto locatieDto;

}
