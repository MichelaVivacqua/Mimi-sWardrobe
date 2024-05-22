package michelavivacqua.Mimi.sWardrobe;

import michelavivacqua.Mimi.sWardrobe.controllers.AbbinamentiController;
import michelavivacqua.Mimi.sWardrobe.controllers.AuthController;
import michelavivacqua.Mimi.sWardrobe.entities.Abbinamento;
import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.entities.Utente;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Ruolo;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import michelavivacqua.Mimi.sWardrobe.payloads.*;
import michelavivacqua.Mimi.sWardrobe.repositories.AbbinamentiDAO;
import michelavivacqua.Mimi.sWardrobe.services.AbbinamentiService;
import michelavivacqua.Mimi.sWardrobe.services.AuthService;
import michelavivacqua.Mimi.sWardrobe.services.IndumentiService;
import michelavivacqua.Mimi.sWardrobe.repositories.IndumentiDAO;
import michelavivacqua.Mimi.sWardrobe.services.UtentiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

@SpringBootTest
class ApplicationTests {


	@Mock
	private IndumentiDAO indumentiDAO;

	@InjectMocks
	private IndumentiService indumentiService;

	@Mock
	private AbbinamentiDAO abbinamentiDAO;

	@InjectMocks
	private AbbinamentiService abbinamentiService;

	@InjectMocks
	private AbbinamentiController abbinamentiController;

	@Mock
	private Authentication authentication;

	@Mock
	private SecurityContext securityContext;


	@Mock
	private AuthService authService;

	@Mock
	private UtentiService utentiService;


	@InjectMocks
	private AuthController authController;

	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetIndumentiList() {
		List<Indumento> indumentiList = Arrays.asList(
				new Indumento("1", "image1.jpg", Colore.BLU, Tipo.PANTALONI, null),
				new Indumento("2", "image2.jpg", Colore.ROSSO, Tipo.CAMICIA, null)
		);
		when(indumentiDAO.findAll()).thenReturn(indumentiList);

		List<Indumento> result = indumentiService.getIndumentiList();

		assertEquals(2, result.size());
		assertEquals("image1.jpg", result.get(0).getImage());
		assertEquals("image2.jpg", result.get(1).getImage());
	}


	@Test
	void testGetAbbinamentiByUtenteId() {
		List<Abbinamento> abbinamentiList = new ArrayList<>();
		Abbinamento abbinamento1 = new Abbinamento();
		abbinamento1.setId(1);
		Abbinamento abbinamento2 = new Abbinamento();
		abbinamento2.setId(2);
		abbinamentiList.add(abbinamento1);
		abbinamentiList.add(abbinamento2);

		int utenteId = 1;
		when(abbinamentiDAO.findByUtenteId(utenteId)).thenReturn(abbinamentiList);

		List<Abbinamento> result = abbinamentiService.getAbbinamentiByUtenteId(utenteId);

		assertEquals(2, result.size());
		assertEquals(1, result.get(0).getId());
		assertEquals(2, result.get(1).getId());
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	void testLogin_ValidData_ShouldReturnToken() throws Exception {
		UtenteLoginDTO loginDTO = new UtenteLoginDTO("user", "password");

		String mockedToken = "mockedToken";
		when(authService.authenticateUserAndGenerateToken(any(UtenteLoginDTO.class)))
				.thenReturn(mockedToken);

		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"user\", \"password\": \"password\"}"))
				.andExpect(status().isOk());
	}

	@Test
	void testRegister_ValidData_ShouldReturn201Created() throws Exception {
		NewUtenteDTO newUtenteDTO = new NewUtenteDTO("username", "name", "surname","email@example.com","https://example.it","123456ciao", Ruolo.USER);

		Utente savedUtente = new Utente();
		savedUtente.setId(1);
		when(utentiService.saveUtente(any(NewUtenteDTO.class))).thenReturn(savedUtente);

		mockMvc.perform(post("/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"username\": \"username\", \"name\": \"name\", \"surname\": \"surname\", \"email\": \"email@example.com\", \"profileImage\": \"https://example.it\", \"password\": \"123456ciao\", \"ruolo\": \"USER\"}"))
				.andExpect(status().isCreated());
	}




}
