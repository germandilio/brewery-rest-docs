package guru.springframework.msscbrewery.web.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.services.v2.BeerServiceV2;
import guru.springframework.msscbrewery.web.controller.v2.BeerControllerV2;
import guru.springframework.msscbrewery.web.model.v2.BeerDtoV2;
import guru.springframework.msscbrewery.web.model.v2.BeerStyleEnum;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;


@ComponentScan(basePackages = "guru.springframework.sfgrestdocsexample.web.mappers")
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerControllerV2.class)
public class BeerControllerV2Test {

  private static class ConstrainedFields {

    private final ConstraintDescriptions constraintDescriptions;

    ConstrainedFields(Class<?> input) {
      this.constraintDescriptions = new ConstraintDescriptions(input);
    }

    private FieldDescriptor withPath(String path) {
      return fieldWithPath(path).attributes(key("constraints").value(StringUtils
          .collectionToDelimitedString(this.constraintDescriptions
              .descriptionsForProperty(path), ". ")));
    }
  }

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private BeerServiceV2 beerServiceV2;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void getBeerById() throws Exception {
    given(beerServiceV2.getBeerById(any())).willReturn(Optional.of(BeerDtoV2.builder().build()));

    mockMvc.perform(get("/api/v2/beer/{beerId}", UUID.randomUUID().toString())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(document("v2/beer-get",
            pathParameters(
                parameterWithName("beerId").description("Beer id")
            ),
            responseFields(
                fieldWithPath("id").description("Beer id"),
                fieldWithPath("beerName").description("Beer name"),
                fieldWithPath("beerStyle").description("Beer style"),
                fieldWithPath("upc").description("Universal product code")
            )
        ));
  }

  @Test
  public void saveNewBeer() throws Exception {
    final BeerDtoV2 beerDtoV2 = BeerDtoV2.builder()
        .beerName("Nice Ale")
        .beerStyle(BeerStyleEnum.ALE)
        .upc(123123123123L)
        .build();

    final String beerDtoJson = objectMapper.writeValueAsString(beerDtoV2);
    ConstrainedFields fields = new ConstrainedFields(BeerDtoV2.class);

    mockMvc.perform(post("/api/v2/beer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(beerDtoJson))
        .andExpect(status().isCreated())
        .andDo(document("v2/beer-post",
            requestFields(
                fields.withPath("id").ignored(),
                fields.withPath("beerName").description("Name of the beer"),
                fields.withPath("beerStyle").description("Style of Beer"),
                fields.withPath("upc").description("Beer Universal product code").attributes()
            )
        ));
  }

  @Test
  public void updateBeer() throws Exception {
    final BeerDtoV2 beerDtoV2 = BeerDtoV2.builder()
        .beerName("Nice Ale")
        .beerStyle(BeerStyleEnum.ALE)
        .upc(123123123123L)
        .build();

    final String beerDtoJson = objectMapper.writeValueAsString(beerDtoV2);
    ConstrainedFields fields = new ConstrainedFields(BeerDtoV2.class);

    mockMvc.perform(put("/api/v2/beer/{beerId}", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(beerDtoJson))
        .andExpect(status().isNoContent())
        .andDo(document("v2/beer-put",
            requestFields(
                fields.withPath("id").description("Beer id"),
                fields.withPath("beerName").description("Name of the beer"),
                fields.withPath("beerStyle").description("Style of Beer"),
                fields.withPath("upc").description("Beer Universal product code").attributes()
            )
        ));
  }

  @Test
  public void deleteBeer() throws Exception {
    mockMvc.perform(delete("/api/v2/beer/{beerId}", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent())
        .andDo(document("v2/beer-delete",
            pathParameters(
                parameterWithName("beerId").description("Beer id")
            )
        ));
  }

}
