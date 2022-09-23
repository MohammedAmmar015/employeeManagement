/**
 *
 * TrainerService class - This is a Service class for Trainer,
 * To validate user input through Util classes 
 * @author Mohammed Ammar
 * @version 1.0 
 * @since 12/08/2022
 *
 **/
package com.ideas2it.employee.service.impl;

import com.ideas2it.employee.constant.Constants;
import com.ideas2it.employee.constant.ErrorMessage;
import com.ideas2it.employee.constant.Attributes;
import com.ideas2it.employee.utilities.StringUtil;
import com.ideas2it.employee.utilities.DateUtil;
import com.ideas2it.employee.dao.impl.TrainerDaoImpl;
import com.ideas2it.employee.dao.inter.TrainerDao;
import com.ideas2it.employee.models.Trainer;
import com.ideas2it.employee.models.Employee;
import com.ideas2it.employee.models.Role;
import com.ideas2it.employee.models.Qualification;
import com.ideas2it.employee.service.inter.TrainerService;
import com.ideas2it.employee.exception.TrainerNotFound;
import com.ideas2it.employee.exception.BadRequest;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class TrainerServiceImpl implements TrainerService {

    private TrainerDao trainerDao = new TrainerDaoImpl();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /**
    * <p>
    * This method is to Validate the Trainer details
    * If all data is Valid. Then, Create object for that
    * and Add it to the List 
    * </p>
    * @param name - Trainer Name
    * @param address - Trainer Address
    * @param mobileNumber - Trainer Mobile Number
    * @param email - Trainer Email
    * @param dateOfJoining - Date, Trainer joined
    * @param dateOfBirth - Trainer Date of Birth
    * @param bloodGroup - Trainer Blood Group
    * @param qualification - Trainer qualification
    * @param trainingExperience - previous training Experience		
    * @return errors
    *         It returns List of Attributes, which failed validation 
    * @throws BadRequest
    *	      Exception will be thrown, If any details get Invalid
    **/
    public List<Attributes> addOrModifyTrainer(Trainer trainer, final String name, final String address, final String mobileNumber,
				    final String email, final String dateOfJoining, final String dateOfBirth,
				    final String qualification, final String bloodGroup, final String trainingExperience) throws BadRequest {
	List<Attributes> errors = new ArrayList<>();
	StringBuilder errorMessage = new StringBuilder();

	String validName = null;
	if (!StringUtil.isValidName(name)) {
	    errors.add(Attributes.NAME);
	    errorMessage.append(ErrorMessage.NAME.errorMessage);
	} else {
	    validName = name;
	}

	Long validMobileNumber = null;
	if (!StringUtil.isValidMobileNumber(mobileNumber)) {
	    errors.add(Attributes.MOBILE_NUMBER);
	    errorMessage.append(ErrorMessage.MOBILE_NUMBER.errorMessage);
	} else {
	    validMobileNumber = Long.valueOf(mobileNumber);
	}

	String validEmail = null;
	if (!StringUtil.isValidEmail(email)) {
	    errors.add(Attributes.EMAIL);
	    errorMessage.append(ErrorMessage.EMAIL.errorMessage);
	} else {
	    validEmail = email;
	}
	
	LocalDate validDateOfJoining = null;
	//if (DateUtil.isValidDateFormat(dateOfJoining)) {
	    validDateOfJoining = LocalDate.parse(dateOfJoining);
	    if (DateUtil.computeDays(validDateOfJoining, LocalDate.now()) < 1) {
	    	errors.add(Attributes.DATE_OF_JOINING);
	    	errorMessage.append(ErrorMessage.DATE_OF_JOINING.errorMessage);
	    }
	/*} else {
	    errors.add(Attributes.DATE_OF_JOINING);
	    errorMessage.append(ErrorMessage.DATE_OF_JOINING.formatErrorMessage);
	}*/

	LocalDate validDateOfBirth = null;
	//if (DateUtil.isValidDateFormat(dateOfJoining)) {
	    validDateOfBirth = LocalDate.parse(dateOfBirth);
	    if (DateUtil.computePeriod(validDateOfBirth, LocalDate.now()) < 18) {
	    	errors.add(Attributes.DATE_OF_BIRTH);
	    	errorMessage.append(ErrorMessage.DATE_OF_BIRTH.errorMessage);
	    }
	/*} else {
	    errors.add(Attributes.DATE_OF_BIRTH);
	    errorMessage.append(ErrorMessage.DATE_OF_BIRTH.formatErrorMessage);
	}*/


	Byte validTrainingExperience = null;
	try {
	    validTrainingExperience = Byte.valueOf(trainingExperience);
	} catch (NumberFormatException e) {
	    errors.add(Attributes.TRAINING_EXPERIENCE);
	    errorMessage.append(ErrorMessage.TRAINING_EXPERIENCE.errorMessage);
	}

	Qualification validQualification;
	Role role;
	Employee employee;
	if (errors.isEmpty()) {
	    if (trainer == null) {
		validQualification = new Qualification(qualification);
	    	role = new Role("Trainer");
	    	employee = new Employee(validName, address, validMobileNumber, validEmail, validDateOfJoining,
				    validDateOfBirth, bloodGroup, validQualification, role);
	    	trainer = new Trainer(employee, validTrainingExperience);
	    } else {
		trainer.getEmployee().getQualification().setDescription(qualification);
		trainer.getEmployee().setName(validName);
		trainer.getEmployee().setAddress(address);
		trainer.getEmployee().setMobileNumber(validMobileNumber);
		trainer.getEmployee().setEmail(validEmail);
		trainer.getEmployee().setDateOfJoining(validDateOfJoining);
		trainer.getEmployee().setDateOfBirth(validDateOfBirth);
		trainer.getEmployee().setBloodGroup(bloodGroup);
		trainer.setTrainingExperience(validTrainingExperience);
	    }
	    trainerDao.insertTrainer(trainer);
	} else {
	    errorMessage.append(errors.size()).append(" Errors Found");
	    errorMessage.append("\t\t\tPlease Re-enter the Trainer details correctly");
	    throw new BadRequest(errors, errorMessage.toString());
	}
	return errors;
    }

    /**
    * <p>
    * This method is used to get and return List of Trainers
    * </p>
    * @return - It returns List of Trainers
    **/
    public List<Trainer> getTrainers() {
	return trainerDao.retrieveTrainers();
    }	

    /**
    * <p>
    * This method is used to get and return One particular Trainer
    * Based on Employee Id
    * </p>
    * @param trainerId - Employee/Trainer Id
    * @return - It returns single Trainer
    **/
    public Trainer getTrainerById(final int trainerId) {
	Trainer trainer = trainerDao.retrieveTrainerById(trainerId);
	if (trainer == null) {
	    throw new TrainerNotFound("Entered trainer id not Found");
	}
	return trainer;
    }

    /**
    * <p>
    * This method is used to remove Trainer object using Trainer Id
    * </p>
    * @param trainerId - Employee/Trainer Id
    * @return - It returns nothing
    * @throws TrainerNotFound
    * 		Exception will be thrown, If Trainer Not found
    **/
    public boolean removeTrainerById(final int trainerId) {
	boolean isDeleted = false;
	if (trainerDao.deleteTrainerById(trainerId)) {
	    isDeleted = true;
	} else {
	    throw new TrainerNotFound(trainerId + " Not Found");
	}
	return isDeleted;
    }
}