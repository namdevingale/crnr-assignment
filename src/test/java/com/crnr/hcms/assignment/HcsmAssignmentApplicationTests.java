package com.crnr.hcms.assignment;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.crnr.hcms.assignment.dto.AddressDto;
import com.crnr.hcms.assignment.dto.PatientDto;
import com.crnr.hcms.assignment.dto.PatientUpdateReqDto;
import com.crnr.hcms.assignment.dto.ResponseDto;
import com.crnr.hcms.assignment.enums.CommonFileds;
import com.crnr.hcms.assignment.service.PatientService;
import com.crnr.hcms.assignment.service.impl.PatientServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HcsmAssignmentApplicationTests {

	@Autowired
	private TestRestTemplate template;

	@InjectMocks
	private PatientService patientService = new PatientServiceImpl();

	@BeforeEach
	public void setUp() {
		patientService = Mockito.mock(PatientServiceImpl.class);
	}

	@LocalServerPort
	int randomServerPort;

	HttpHeaders headers = new HttpHeaders();

	@Test
	@DisplayName("Create Patient")
	public void testCreatePatient() throws URISyntaxException {
		PatientDto patientDto = getPatientDto();
		URI uri = new URI(createURLWithPort("/v1/api/patients"));
		HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, headers);
		ResponseEntity<ResponseDto> responseDto = template.postForEntity(uri, request, ResponseDto.class);
		Assert.assertEquals("Successfully created the Patient", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.CREATED, responseDto.getBody().getStatus());
	}
	
	@Test
	@DisplayName("Update Patient")
	public void testUpdatePatient() throws URISyntaxException {
		PatientUpdateReqDto patientDto = getPatientUpdateDto();
		patientDto.setName("Paul");
		URI uri = new URI(createURLWithPort("/v1/api/patients/PT_00017"));
		HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(patientDto, headers);
		ResponseEntity<ResponseDto> responseDto = template.exchange(uri, HttpMethod.PUT, request, ResponseDto.class);
		Assert.assertEquals("Successfully updated the Patient", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.OK, responseDto.getBody().getStatus());
	}
	
	@Test
	@DisplayName("Update Patient")
	public void testUpdatePatientForNotFound() throws URISyntaxException {
		PatientUpdateReqDto patientDto = getPatientUpdateDto();
		patientDto.setName("Paul");
		URI uri = new URI(createURLWithPort("/v1/api/patients/PT_0000"));
		HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(patientDto, headers);
		ResponseEntity<ResponseDto> responseDto = template.exchange(uri, HttpMethod.PUT, request, ResponseDto.class);
		Assert.assertEquals("Record not found", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseDto.getBody().getStatus());
	}
	
	@Test
	@DisplayName("Delete Patient")
	public void testDeletePatient() throws URISyntaxException {
		URI uri = new URI(createURLWithPort("/v1/api/patients/PT_00019"));
		HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseDto> responseDto = template.exchange(uri, HttpMethod.DELETE, request, ResponseDto.class);
		Assert.assertEquals("Successfully deleted the Patient", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.OK, responseDto.getBody().getStatus());
	}
	
	@Test
	@DisplayName("Delete Patient")
	public void testDeletePatientForNotFound() throws URISyntaxException {
		URI uri = new URI(createURLWithPort("/v1/api/patients/PT_0000"));
		HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(headers);
		ResponseEntity<ResponseDto> responseDto = template.exchange(uri, HttpMethod.DELETE, request, ResponseDto.class);
		Assert.assertEquals("Record not found", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseDto.getBody().getStatus());
	}

	@Test
	@DisplayName("Get Patient by Id")
	public void testFindPatientById() {
		ResponseEntity<ResponseDto> responseDto = template.getForEntity("/v1/api/patients/PT_00016", ResponseDto.class);
		Assert.assertEquals("Successfully fetched the Patient", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.OK, responseDto.getBody().getStatus());
	}

	@Test
	@DisplayName("Get Patient by Id")
	public void testFindPatientByIdForNotFound() {
		ResponseEntity<ResponseDto> responseDto = template.getForEntity("/v1/api/patients/PT_0000", ResponseDto.class);
		Assert.assertEquals("Record not found", responseDto.getBody().getMessage());
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseDto.getBody().getStatus());
	}

	@Test
	@DisplayName("Get Patients by filter")
	public void testFindPatientsByFilterWithDescOrder() {
		ResponseEntity<ResponseDto> responseDto = template.getForEntity(
				"/v1/api/patients/patientbyfilter?page=0&size=10&order=desc&search=ab&status=active",
				ResponseDto.class);
		Assert.assertEquals("Patients data get Successfully", responseDto.getBody().getMessage());
	}

	@Test
	@DisplayName("Get Patients by filter")
	public void testFindPatientsByFilterWithAscOrder() {
		ResponseEntity<ResponseDto> responseDto = template.getForEntity(
				"/v1/api/patients/patientbyfilter?page=0&size=10&order=asc&gender=male", ResponseDto.class);
		Assert.assertEquals("Patients data get Successfully", responseDto.getBody().getMessage());
	}

	@Test
	@DisplayName("Get Patient by Id")
	public void testFindPatientByIdForNotFoundWithMockito() {
		Mockito.when(patientService.findById(Mockito.anyString())).thenReturn(getSampleResponseDto());
		ResponseEntity<ResponseDto> responseDto = patientService.findById("PT_00016");
		Assert.assertEquals(HttpStatus.NOT_FOUND, responseDto.getBody().getStatus());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + randomServerPort + uri;
	}

	private ResponseEntity<ResponseDto> getSampleResponseDto() {
		ResponseDto responseDto = null;
		responseDto = ResponseDto.builder().action("findById").result(CommonFileds.FAILURE.getFetchType())
				.message("Record not found").data("").code(404).status(HttpStatus.NOT_FOUND).build();
		return new ResponseEntity<ResponseDto>(responseDto, responseDto.getStatus());
	}

	private PatientDto getPatientDto() {
		PatientDto patientDto = new PatientDto();
		patientDto.setName("patient" + Long.valueOf(new SecureRandom().nextInt(9999)));
		patientDto.setGender("MALE");
		String dateString = "1995-08-16 00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
		patientDto.setDateOfBirth(dateTime);
		patientDto.setPrimaryContactCountryCode("+91");
		patientDto.setPrimaryContactNumber(9867854321L);
		patientDto.setSecondaryContactCountryCode("+91");
		patientDto.setSecondaryContactNumber(9868760378L);
		List<AddressDto> addressDtoLst = new ArrayList<AddressDto>();
		AddressDto addressDto = new AddressDto();
		addressDto.setAddressLineOne("#"+Long.valueOf(new SecureRandom().nextInt(9999))+", Kengeri");
		addressDto.setAddressLineTwo("Kommaghatta main road");
		addressDto.setAddressType("PRESENT");
		addressDto.setCity("Bengaluru");
		addressDto.setCountry("India");
		addressDto.setState("Karnataka");
		addressDto.setPinCode(560060L);
		addressDtoLst.add(addressDto);
		patientDto.setAddresses(addressDtoLst);
		return patientDto;
	}

	private PatientUpdateReqDto getPatientUpdateDto() {
		PatientUpdateReqDto patientDto = new PatientUpdateReqDto();
		patientDto.setName("patient" + Long.valueOf(new SecureRandom().nextInt(9999)));
		patientDto.setGender("MALE");
		String dateString = "1999-03-23 00:00:00";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
		patientDto.setDateOfBirth(dateTime);
		patientDto.setPrimaryContactCountryCode("+91");
		patientDto.setPrimaryContactNumber(9867854321L);
		patientDto.setSecondaryContactCountryCode("+91");
		patientDto.setSecondaryContactNumber(9868760378L);
		List<AddressDto> addressDtoLst = new ArrayList<AddressDto>();
		AddressDto addressDto = new AddressDto();
		addressDto.setAddressId(29);
		addressDto.setAddressLineOne("#"+Long.valueOf(new SecureRandom().nextInt(9999))+", Kengeri");
		addressDto.setAddressLineTwo("Kommaghatta main road");
		addressDto.setAddressType("PERMANENT");
		addressDto.setCity("Bengaluru");
		addressDto.setCountry("India");
		addressDto.setState("Karnataka");
		addressDto.setPinCode(560060L);
		addressDtoLst.add(addressDto);
		AddressDto addressDto1 = new AddressDto();
		addressDto1.setAddressId(30);
		addressDto1.setAddressLineOne("#"+Long.valueOf(new SecureRandom().nextInt(9999))+", Jaynagar");
		addressDto1.setAddressLineTwo("near BMTC bust stand");
		addressDto1.setAddressType("OFFICE");
		addressDto1.setCity("Bengaluru");
		addressDto1.setCountry("India");
		addressDto1.setState("Karnataka");
		addressDto1.setPinCode(560039L);
		addressDtoLst.add(addressDto1);
		patientDto.setAddressList(addressDtoLst);
		return patientDto;
	}
}
