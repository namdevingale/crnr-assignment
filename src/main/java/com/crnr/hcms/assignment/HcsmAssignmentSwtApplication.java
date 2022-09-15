package com.crnr.hcms.assignment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * SWT ui for patient crud operations
 *
 * @author Namadev
 * 
 * @version 1.0
 */
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.crnr.hcms.assignment.dto.AddressDto;
import com.crnr.hcms.assignment.dto.AddressTextDto;
import com.crnr.hcms.assignment.dto.PatientDto;
import com.crnr.hcms.assignment.dto.PatientUpdateReqDto;
import com.crnr.hcms.assignment.dto.ResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.eclipse.swt.events.SelectionListener.*;

public class HcsmAssignmentSwtApplication {

	public HcsmAssignmentSwtApplication(Shell shell, ArrayList<PatientDto> patientDtoLst) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		ObjectMapper mapper = new ObjectMapper();

		final Table table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		final int columnCount = 7;
		for (int i = 0; i < columnCount; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			if (i == 0)
				column.setText("Patient Id");
			if (i == 1)
				column.setText("Patient Name");
			if (i == 2)
				column.setText("Date of Birth");
			if (i == 3)
				column.setText("Gender");
			if (i == 4)
				column.setText("Action");
			if (i == 5)
				column.setText("Action");
			if (i == 6)
				column.setText("Action");
		}
		for (int i = 0; i < patientDtoLst.size(); i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			for (int j = 0; j < columnCount; j++) {
				if (j == 0)
					item.setText(j, patientDtoLst.get(i).getPatientId());
				if (j == 1)
					item.setText(j, patientDtoLst.get(i).getName());
				if (j == 2)
					item.setText(j, patientDtoLst.get(i).getDateOfBirth());
				if (j == 3)
					item.setText(j, patientDtoLst.get(i).getGender());
				if (j == 4)
					item.setText(j, "View");
				if (j == 5)
					item.setText(j, "Update");
				if (j == 6)
					item.setText(j, "Delete");
			}
		}
		for (int i = 0; i < columnCount; i++) {
			table.getColumn(i).pack();
		}
		Rectangle clientArea = shell.getClientArea();
		table.setLocation(clientArea.x, clientArea.y);
		Point size = table.computeSize(SWT.DEFAULT, 200);
		table.setSize(size);
		shell.pack();
		table.addListener(SWT.MouseDown, event -> {
			Point pt = new Point(event.x, event.y);
			TableItem item = table.getItem(pt);
			if (item == null)
				return;
			for (int i = 0; i < columnCount; i++) {
				Rectangle rect = item.getBounds(i);
				if (rect.contains(pt)) {
					int index = table.indexOf(item);
					String uri = "http://localhost:9090/v1/api/patients/" + patientDtoLst.get(index).getPatientId();
					ResponseEntity<ResponseDto> viewResponse = restTemplate.getForEntity(uri, ResponseDto.class);
					PatientDto viewPatientDto = mapper.convertValue(viewResponse.getBody().getData(), PatientDto.class);
					if (i == 4) {
						final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
						dialog.setText("View Patient");
						dialog.setLayout(new GridLayout(2, false));

						Label label15 = new Label(dialog, SWT.NULL);
						label15.setText("Patient Id :- ");
						Text patientId = new Text(dialog, SWT.READ_ONLY);
						patientId.setText(viewPatientDto.getPatientId());

						Label label1 = new Label(dialog, SWT.NULL);
						label1.setText("Patient Name :- ");
						Text patientName = new Text(dialog, SWT.READ_ONLY);
						patientName.setText(viewPatientDto.getName());

						Label label2 = new Label(dialog, SWT.NULL);
						label2.setText("Date of Birth :- ");
						Text dateOfBirth = new Text(dialog, SWT.READ_ONLY);
						dateOfBirth.setText(viewPatientDto.getDateOfBirth());

						Label label3 = new Label(dialog, SWT.NULL);
						label3.setText("Gender :- ");
						Text gender = new Text(dialog, SWT.READ_ONLY);
						gender.setText(viewPatientDto.getGender());

						Label label4 = new Label(dialog, SWT.NULL);
						label4.setText("Country Code :- ");
						Text primaryContactCountryCode = new Text(dialog, SWT.READ_ONLY);
						primaryContactCountryCode.setText(viewPatientDto.getPrimaryContactCountryCode());

						Label label5 = new Label(dialog, SWT.NULL);
						label5.setText("Primary Contact No. :- ");
						Text primaryContactNumber = new Text(dialog, SWT.READ_ONLY);
						primaryContactNumber.setText(viewPatientDto.getPrimaryContactNumber().toString());

						Label label6 = new Label(dialog, SWT.NULL);
						label6.setText("Country Code :- ");
						Text secondaryContactCountryCode = new Text(dialog, SWT.READ_ONLY);
						secondaryContactCountryCode.setText(viewPatientDto.getSecondaryContactCountryCode());

						Label label7 = new Label(dialog, SWT.NULL);
						label7.setText("Secondary Contact No. :- ");
						Text secondaryContactNumber = new Text(dialog, SWT.READ_ONLY);
						if (viewPatientDto.getSecondaryContactNumber() != null)
							secondaryContactNumber.setText(viewPatientDto.getSecondaryContactNumber().toString());
						else
							secondaryContactNumber.setText("");

						for (int k = 0; k < viewPatientDto.getAddresses().size(); k++) {
							Label label8 = new Label(dialog, SWT.NULL);
							label8.setText("Address Type :- ");
							Text addressType = new Text(dialog, SWT.READ_ONLY);
							addressType.setText(viewPatientDto.getAddresses().get(k).getAddressType());

							Label label9 = new Label(dialog, SWT.NULL);
							label9.setText("Address Line 1 :- ");
							Text addressLineOne = new Text(dialog, SWT.READ_ONLY);
							addressLineOne.setText(viewPatientDto.getAddresses().get(k).getAddressLineOne());

							Label label10 = new Label(dialog, SWT.NULL);
							label10.setText("Address Line 2 :- ");
							Text addressLineTwo = new Text(dialog, SWT.READ_ONLY);
							addressLineTwo.setText(viewPatientDto.getAddresses().get(k).getAddressLineTwo());

							Label label11 = new Label(dialog, SWT.NULL);
							label11.setText("City :- ");
							Text city = new Text(dialog, SWT.READ_ONLY);
							city.setText(viewPatientDto.getAddresses().get(k).getCity());

							Label label12 = new Label(dialog, SWT.NULL);
							label12.setText("State :- ");
							Text state = new Text(dialog, SWT.READ_ONLY);
							state.setText(viewPatientDto.getAddresses().get(k).getState());

							Label label13 = new Label(dialog, SWT.NULL);
							label13.setText("Country :- ");
							Text country = new Text(dialog, SWT.READ_ONLY);
							country.setText(viewPatientDto.getAddresses().get(k).getCountry());

							Label label14 = new Label(dialog, SWT.NULL);
							label14.setText("Pin Code :- ");
							Text pinCode = new Text(dialog, SWT.READ_ONLY);
							pinCode.setText(viewPatientDto.getAddresses().get(k).getPinCode().toString());
						}
						Button ok = new Button(dialog, SWT.PUSH);
						ok.setText("OK");
						ok.addSelectionListener(widgetSelectedAdapter(event2 -> {
							dialog.close();
						}));
						dialog.setDefaultButton(ok);
						dialog.pack();
						dialog.open();
					}
					if (i == 5) {
						final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
						dialog.setText("Update Patient");
						dialog.setLayout(new GridLayout(2, false));

						Label label15 = new Label(dialog, SWT.NULL);
						label15.setText("Patient Id : ");
						Text patientId = new Text(dialog, SWT.READ_ONLY);
						patientId.setText(viewPatientDto.getPatientId());

						Label label1 = new Label(dialog, SWT.NULL);
						label1.setText("Patient Name : ");
						Text patientName = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						patientName.setText(viewPatientDto.getName());
						patientName.setTextLimit(20);

						Label label2 = new Label(dialog, SWT.NULL);
						label2.setText("DOB(yyyy-mm-dd hh:mm) : ");
						Text dateOfBirth = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						dateOfBirth.setText(viewPatientDto.getDateOfBirth());
						dateOfBirth.setTextLimit(20);

						Label label3 = new Label(dialog, SWT.NULL);
						label3.setText("Gender : ");
						Text gender = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						gender.setText(viewPatientDto.getGender());
						gender.setTextLimit(15);

						Label label4 = new Label(dialog, SWT.NULL);
						label4.setText("Country Code : ");
						Text primaryContactCountryCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						primaryContactCountryCode.setText(viewPatientDto.getPrimaryContactCountryCode());
						primaryContactCountryCode.setTextLimit(10);

						Label label5 = new Label(dialog, SWT.NULL);
						label5.setText("Primary Contact No. : ");
						Text primaryContactNumber = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						primaryContactNumber.setText(viewPatientDto.getPrimaryContactNumber().toString());
						primaryContactNumber.setTextLimit(12);

						Label label6 = new Label(dialog, SWT.NULL);
						label6.setText("Country Code : ");
						Text secondaryContactCountryCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						secondaryContactCountryCode.setText(viewPatientDto.getSecondaryContactCountryCode());
						secondaryContactCountryCode.setTextLimit(10);

						Label label7 = new Label(dialog, SWT.NULL);
						label7.setText("Secondary Contact No. : ");
						Text secondaryContactNumber = new Text(dialog, SWT.SINGLE | SWT.BORDER);
						if (viewPatientDto.getSecondaryContactNumber() != null)
							secondaryContactNumber.setText(viewPatientDto.getSecondaryContactNumber().toString());
						else
							secondaryContactNumber.setText("");
						secondaryContactNumber.setTextLimit(12);

						List<AddressTextDto> addressTextDtoLst = new ArrayList<AddressTextDto>();
						for (int k = 0; k < viewPatientDto.getAddresses().size(); k++) {
							AddressTextDto addressTextDto = new AddressTextDto();
							Label label17 = new Label(dialog, SWT.NULL);
							label17.setText("Address Id : ");
							Text addressId = new Text(dialog, SWT.READ_ONLY);
							addressId.setText(String.valueOf(viewPatientDto.getAddresses().get(k).getAddressId()));
							addressTextDto.setAddressId(addressId);

							Label label8 = new Label(dialog, SWT.NULL);
							label8.setText("Address Type : ");
							Text addressType = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							addressType.setText(viewPatientDto.getAddresses().get(k).getAddressType());
							addressType.setTextLimit(10);
							addressTextDto.setAddressType(addressType);

							Label label9 = new Label(dialog, SWT.NULL);
							label9.setText("Address Line 1 : ");
							Text addressLineOne = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							addressLineOne.setText(viewPatientDto.getAddresses().get(k).getAddressLineOne());
							addressLineOne.setTextLimit(50);
							addressTextDto.setAddressLineOne(addressLineOne);

							Label label10 = new Label(dialog, SWT.NULL);
							label10.setText("Address Line 2 : ");
							Text addressLineTwo = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							addressLineTwo.setText(viewPatientDto.getAddresses().get(k).getAddressLineTwo());
							addressLineTwo.setTextLimit(50);
							addressTextDto.setAddressLineTwo(addressLineTwo);

							Label label11 = new Label(dialog, SWT.NULL);
							label11.setText("City : ");
							Text city = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							city.setText(viewPatientDto.getAddresses().get(k).getCity());
							city.setTextLimit(20);
							addressTextDto.setCity(city);

							Label label12 = new Label(dialog, SWT.NULL);
							label12.setText("State : ");
							Text state = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							state.setText(viewPatientDto.getAddresses().get(k).getState());
							state.setTextLimit(20);
							addressTextDto.setState(state);

							Label label13 = new Label(dialog, SWT.NULL);
							label13.setText("Country : ");
							Text country = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							country.setText(viewPatientDto.getAddresses().get(k).getCountry());
							country.setTextLimit(20);
							addressTextDto.setCountry(country);

							Label label14 = new Label(dialog, SWT.NULL);
							label14.setText("Pin Code : ");
							Text pinCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
							pinCode.setText(viewPatientDto.getAddresses().get(k).getPinCode().toString());
							pinCode.setTextLimit(10);
							addressTextDto.setPinCode(pinCode);
							addressTextDtoLst.add(addressTextDto);
						}
						Button ok = new Button(dialog, SWT.PUSH);
						ok.setText("Update Patient");
						ok.addSelectionListener(widgetSelectedAdapter(event2 -> {
							if (patientName.getText() == "") {
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Please enter the patient name");
								messageBox.open();
							} else if (dateOfBirth.getText() == "") {
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Please enter the date of birth");
								messageBox.open();
							} else if (gender.getText() == "") {
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Please enter the gender");
								messageBox.open();
							} else if (primaryContactCountryCode.getText() == "") {
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Please enter the primary contact country code");
								messageBox.open();
							} else if (primaryContactNumber.getText() == "") {
								MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
								messageBox.setMessage("Please enter the primary contact number");
								messageBox.open();
							} else {
								PatientUpdateReqDto patientUpdateReqDto = new PatientUpdateReqDto();
								patientUpdateReqDto.setName(patientName.getText());
								String dob = dateOfBirth.getText().replace("T", " ");
								patientUpdateReqDto.setDateOfBirth(dob);
								patientUpdateReqDto.setGender(gender.getText());
								patientUpdateReqDto.setPrimaryContactCountryCode(primaryContactCountryCode.getText());
								patientUpdateReqDto
										.setPrimaryContactNumber(Long.parseLong(primaryContactNumber.getText()));
								patientUpdateReqDto
										.setSecondaryContactCountryCode(secondaryContactCountryCode.getText());
								if (!secondaryContactNumber.getText().isEmpty())
									patientUpdateReqDto.setSecondaryContactNumber(
											Long.parseLong(secondaryContactNumber.getText()));
								List<AddressDto> addressDtoLst = new ArrayList<AddressDto>();
								for (AddressTextDto addressTextDto : addressTextDtoLst) {
									AddressDto addressDto = new AddressDto();
									addressDto.setAddressId(Long.parseLong(addressTextDto.getAddressId().getText()));
									addressDto.setAddressType(addressTextDto.getAddressType().getText());
									addressDto.setAddressLineOne(addressTextDto.getAddressLineOne().getText());
									addressDto.setAddressLineTwo(addressTextDto.getAddressLineTwo().getText());
									addressDto.setCity(addressTextDto.getCity().getText());
									addressDto.setState(addressTextDto.getState().getText());
									addressDto.setCountry(addressTextDto.getCountry().getText());
									addressDto.setPinCode(Long.parseLong(addressTextDto.getPinCode().getText()));
									addressDtoLst.add(addressDto);
								}
								patientUpdateReqDto.setAddressList(addressDtoLst);
								HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(patientUpdateReqDto,
										headers);
								ResponseEntity<ResponseDto> updateResponse = restTemplate.exchange(uri, HttpMethod.PUT,
										request, ResponseDto.class);
								MessageBox messageBox = new MessageBox(shell, SWT.OK);
								messageBox.setMessage(updateResponse.getBody().getMessage());
								messageBox.open();
								dialog.close();
							}
						}));
						dialog.setDefaultButton(ok);
						dialog.pack();
						dialog.open();
					}
					if (i == 6) {
						HttpEntity<PatientUpdateReqDto> request = new HttpEntity<>(headers);
						ResponseEntity<ResponseDto> deleteResponse = restTemplate.exchange(uri, HttpMethod.DELETE,
								request, ResponseDto.class);
						MessageBox messageBox = new MessageBox(shell, SWT.OK);
						messageBox.setMessage(deleteResponse.getBody().getMessage());
						messageBox.open();
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		HttpHeaders headers = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:9090/v1/api/patients/patientbyfilter?page=0&size=9999999&order=desc";
		ResponseEntity<ResponseDto> response = restTemplate.getForEntity(url, ResponseDto.class);
		try {
			ObjectMapper mapper = new ObjectMapper();
			ArrayList<PatientDto> patientDtoLst = mapper.convertValue(response.getBody().getData(),
					new TypeReference<ArrayList<PatientDto>>() {
					});
			Display display = new Display();
			Shell shell = new Shell(display);
			shell.setText("Healthcare Management System");
			shell.setLayout(new GridLayout(1, false));
			Text searchText = new Text(shell, SWT.SINGLE | SWT.BORDER);
			searchText.setText("");
			Button searchButton = new Button(shell, SWT.PUSH);
			searchButton.setText("Search Patients");
			searchButton.addSelectionListener(widgetSelectedAdapter(event3 -> {
				if (searchText.getText() == "") {
					MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING | SWT.CANCEL);
					messageBox.setMessage("Please enter the search string");
					messageBox.open();
				} else {
					String searchUrl = "http://localhost:9090/v1/api/patients/patientbyfilter?page=0&size=9999999&order=desc&search="
							+ searchText.getText();
					ResponseEntity<ResponseDto> searchResponse = restTemplate.getForEntity(searchUrl,
							ResponseDto.class);
					ArrayList<PatientDto> searchPatientDtoLst = mapper.convertValue(searchResponse.getBody().getData(),
							new TypeReference<ArrayList<PatientDto>>() {
							});
					new HcsmAssignmentSwtApplication(shell, searchPatientDtoLst);
				}
			}));

			Button createButton = new Button(shell, SWT.PUSH);
			createButton.setText("Create Patients");
			createButton.addSelectionListener(widgetSelectedAdapter(event4 -> {

				final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
				dialog.setText("Create Patient");
				dialog.setLayout(new GridLayout(2, false));

				Label label1 = new Label(dialog, SWT.NULL);
				label1.setText("Patient Name : ");
				Text patientName = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				patientName.setText("");
				patientName.setTextLimit(20);

				Label label2 = new Label(dialog, SWT.NULL);
				label2.setText("DOB(yyyy-mm-dd hh:mm) : ");
				Text dateOfBirth = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				dateOfBirth.setText("");
				dateOfBirth.setTextLimit(20);

				Label label3 = new Label(dialog, SWT.NULL);
				label3.setText("Gender : ");
				Text gender = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				gender.setText("");
				gender.setTextLimit(15);

				Label label4 = new Label(dialog, SWT.NULL);
				label4.setText("Country Code : ");
				Text primaryContactCountryCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				primaryContactCountryCode.setText("");
				primaryContactCountryCode.setTextLimit(10);

				Label label5 = new Label(dialog, SWT.NULL);
				label5.setText("Primary Contact No. : ");
				Text primaryContactNumber = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				primaryContactNumber.setText("");
				primaryContactNumber.setTextLimit(12);

				Label label6 = new Label(dialog, SWT.NULL);
				label6.setText("Country Code : ");
				Text secondaryContactCountryCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				secondaryContactCountryCode.setText("");
				secondaryContactCountryCode.setTextLimit(10);

				Label label7 = new Label(dialog, SWT.NULL);
				label7.setText("Secondary Contact No. : ");
				Text secondaryContactNumber = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				secondaryContactNumber.setText("");
				secondaryContactNumber.setTextLimit(12);

				Label label8 = new Label(dialog, SWT.NULL);
				label8.setText("Address Type : ");
				Text addressType = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				addressType.setText("");
				addressType.setTextLimit(10);

				Label label9 = new Label(dialog, SWT.NULL);
				label9.setText("Address Line 1 : ");
				Text addressLineOne = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				addressLineOne.setText("");
				addressLineOne.setTextLimit(50);

				Label label10 = new Label(dialog, SWT.NULL);
				label10.setText("Address Line 2 : ");
				Text addressLineTwo = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				addressLineTwo.setText("");
				addressLineTwo.setTextLimit(50);

				Label label11 = new Label(dialog, SWT.NULL);
				label11.setText("City : ");
				Text city = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				city.setText("");
				city.setTextLimit(20);

				Label label12 = new Label(dialog, SWT.NULL);
				label12.setText("State : ");
				Text state = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				state.setText("");
				state.setTextLimit(20);

				Label label13 = new Label(dialog, SWT.NULL);
				label13.setText("Country : ");
				Text country = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				country.setText("");
				country.setTextLimit(20);

				Label label14 = new Label(dialog, SWT.NULL);
				label14.setText("Pin Code : ");
				Text pinCode = new Text(dialog, SWT.SINGLE | SWT.BORDER);
				pinCode.setText("");
				pinCode.setTextLimit(10);

				Button ok = new Button(dialog, SWT.PUSH);
				ok.setText("Create");
				ok.addSelectionListener(widgetSelectedAdapter(event2 -> {
					if (patientName.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the patient name");
						messageBox.open();
					} else if (dateOfBirth.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the date of birth");
						messageBox.open();
					} else if (gender.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the gender");
						messageBox.open();
					} else if (primaryContactCountryCode.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the primary contact country code");
						messageBox.open();
					} else if (primaryContactNumber.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the primary contact number");
						messageBox.open();
					} else if (addressType.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the address type");
						messageBox.open();
					} else if (addressLineOne.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the address line 1");
						messageBox.open();
					} else if (city.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the city");
						messageBox.open();
					} else if (state.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the state");
						messageBox.open();
					} else if (country.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the country");
						messageBox.open();
					} else if (pinCode.getText() == "") {
						MessageBox messageBox = new MessageBox(shell, SWT.OK | SWT.ICON_WARNING);
						messageBox.setMessage("Please enter the pin code");
						messageBox.open();
					} else {
						String createUrl = "http://localhost:9090/v1/api/patients";
						PatientDto patientDto = new PatientDto();
						patientDto.setName(patientName.getText());
						patientDto.setDateOfBirth(dateOfBirth.getText());
						patientDto.setGender(gender.getText());
						patientDto.setPrimaryContactCountryCode(primaryContactCountryCode.getText());
						patientDto.setPrimaryContactNumber(Long.parseLong(primaryContactNumber.getText()));
						patientDto.setSecondaryContactCountryCode(secondaryContactCountryCode.getText());
						if (!secondaryContactNumber.getText().isEmpty())
							patientDto.setSecondaryContactNumber(Long.parseLong(secondaryContactNumber.getText()));
						ArrayList<AddressDto> addressDtoLst = new ArrayList<AddressDto>();
						AddressDto addressDto = new AddressDto();
						addressDto.setAddressType(addressType.getText());
						addressDto.setAddressLineOne(addressLineOne.getText());
						addressDto.setAddressLineTwo(addressLineTwo.getText());
						addressDto.setCity(city.getText());
						addressDto.setState(state.getText());
						addressDto.setCountry(country.getText());
						addressDto.setPinCode(Long.parseLong(pinCode.getText()));
						addressDtoLst.add(addressDto);
						patientDto.setAddresses(addressDtoLst);
						HttpEntity<PatientDto> request = new HttpEntity<>(patientDto, headers);
						ResponseEntity<ResponseDto> responseDto = restTemplate.postForEntity(createUrl, request,
								ResponseDto.class);
						MessageBox messageBox = new MessageBox(shell, SWT.OK);
						messageBox.setMessage(responseDto.getBody().getMessage());
						messageBox.open();
						dialog.close();
					}
				}));
				dialog.setDefaultButton(ok);
				dialog.pack();
				dialog.open();
			}));
			shell.setDefaultButton(searchButton);
			new HcsmAssignmentSwtApplication(shell, patientDtoLst);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		} catch (Exception e) {
			System.out.println("Error in Api calling " + e.getMessage());
		}
	}
}
