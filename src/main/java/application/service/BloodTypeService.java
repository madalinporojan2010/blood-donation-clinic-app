package application.service;

import application.model.BloodType;
import application.model.repository.BloodTypeRepositoryModels;
import application.model.response.StatusResponse;
import application.repository.jpa.BloodTypeRepositoryJPA;
import application.repository.jpa.mysql.IBloodTypeRepository;
import application.utils.ResponseMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class used for the blood type table.
 * Usual calls to the Repository Class, fetching database table data.
 */
@Service
public class BloodTypeService {

    private final BloodTypeRepositoryModels bloodTypeRepositoryModels;

    /**
     * BloodTypeService constructor used for repositories initialization.
     * JPA only.
     * 
     * @param iBloodTypeRepository Blood Type table repository
     */
    @Autowired(required = true)
    public BloodTypeService(IBloodTypeRepository iBloodTypeRepository) {
        this.bloodTypeRepositoryModels = new BloodTypeRepositoryJPA(iBloodTypeRepository);
    }

    /**
     * BloodTypeService constructor used for repositories initialization.
     * Generic.
     * 
     * @param bloodTypeRepositoryModels Blood Type table models
     */
    public BloodTypeService(BloodTypeRepositoryModels bloodTypeRepositoryModels) {
        this.bloodTypeRepositoryModels = bloodTypeRepositoryModels;
    }

    /**
     * Fetches all the entries from the bloodType table.
     *
     * @return Returns fetched BloodTypes entries in a List.
     */
    public List<BloodType> findAllBloodTypes() {
        List<BloodType> fetchedBloodType = null;
        try {
            fetchedBloodType = bloodTypeRepositoryModels.findAll();
        } catch (Exception e) {
            ResponseMessage.printMethodErrorString(this.getClass(), e);
        }
        return fetchedBloodType;
    }

    /**
     * Saves a bloodType request to bloodType table.
     *
     * @param bloodType Given BloodType request body.
     * @return Success or error message.
     */
    public StatusResponse saveBloodType(BloodType bloodType) {
        StatusResponse statusResponse = new StatusResponse();
        try {
            bloodTypeRepositoryModels.save(bloodType);
            statusResponse.setMessage(ResponseMessage.SUCCESS);
        } catch (Exception e) {
            ResponseMessage.printMethodErrorString(this.getClass(), e);
            statusResponse.setMessage(ResponseMessage.ERROR);
        }
        return statusResponse;
    }

    /**
     * Updates a bloodType entry with the given request body.
     *
     * @param bloodType Given bloodType request body.
     * @return Success or error message.
     */
    public StatusResponse updateBloodType(BloodType bloodType) {
        StatusResponse statusResponse = new StatusResponse();
        try {
            if (bloodTypeRepositoryModels.existsById(bloodType.getId())) {
                bloodTypeRepositoryModels.save(bloodType);
                statusResponse.setMessage(ResponseMessage.SUCCESS);
            } else {
                statusResponse.setMessage(ResponseMessage.ERROR_ENTRY_NOT_PRESENT);
            }
        } catch (Exception e) {
            ResponseMessage.printMethodErrorString(this.getClass(), e);
            statusResponse.setMessage(ResponseMessage.ERROR);
        }
        return statusResponse;
    }

    /**
     * Deletes a bloodType entry with the given id.
     *
     * @param bloodTypeId Given bloodType id.
     * @return Success or error message.
     */
    public StatusResponse deleteBloodType(Long bloodTypeId) {
        StatusResponse statusResponse = new StatusResponse();
        try {
            if (bloodTypeRepositoryModels.existsById(bloodTypeId)) {
                bloodTypeRepositoryModels.deleteById(bloodTypeId);
                statusResponse.setMessage(ResponseMessage.SUCCESS);
            } else {
                statusResponse.setMessage(ResponseMessage.ERROR_ENTRY_NOT_PRESENT);
            }
        } catch (Exception e) {
            ResponseMessage.printMethodErrorString(this.getClass(), e);
            statusResponse.setMessage(ResponseMessage.ERROR);
        }
        return statusResponse;
    }
}
