package michelavivacqua.Mimi.sWardrobe;

import michelavivacqua.Mimi.sWardrobe.entities.Indumento;
import michelavivacqua.Mimi.sWardrobe.enums.Colore;
import michelavivacqua.Mimi.sWardrobe.enums.Tipo;
import michelavivacqua.Mimi.sWardrobe.payloads.NewIndumentoDTO;
import michelavivacqua.Mimi.sWardrobe.services.IndumentiService;
import michelavivacqua.Mimi.sWardrobe.repositories.IndumentiDAO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ApplicationTests {

	@Mock
	private IndumentiDAO indumentiDAO;

	@InjectMocks
	private IndumentiService indumentiService;

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

//	@Test
//	void testSaveIndumento() {
//		Authentication authentication = Mockito.mock(Authentication.class);
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//
//		NewIndumentoDTO newIndumentoDTO = new NewIndumentoDTO("1","image.jpg", Colore.BLU, Tipo.PANTALONI);
//
//		Indumento savedIndumento = new Indumento("1", "image.jpg", Colore.BLU, Tipo.PANTALONI, null);
//		when(indumentiDAO.save(any(Indumento.class))).thenReturn(savedIndumento);
//
//		Indumento result = indumentiService.saveIndumento(newIndumentoDTO);
//
//		assertEquals("image.jpg", result.getImage());
//		assertEquals(Colore.BLU, result.getColore());
//		assertEquals(Tipo.PANTALONI, result.getTipo());
//	}

}
