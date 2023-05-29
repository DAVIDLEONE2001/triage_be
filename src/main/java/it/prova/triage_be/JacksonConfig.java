package it.prova.triage_be;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.io.IOException;
import java.util.List;
import it.prova.triage_be.dto.DottoreResponseDTO;


@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule springDataPageModule() {
        return new SimpleModule().addDeserializer(Page.class, new PageDeserializer());
    }

    private static class PageDeserializer extends JsonDeserializer<Page> {

        @Override
        public Page deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException, JsonProcessingException {
            // Leggi i valori dalla risposta JSON e crea un oggetto Page concreto
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(jsonParser);
            int pageNo = node.get("pageNo").asInt();

            int pageSize = node.get("pageSize").asInt();
            String sortBy = node.get("sortBy").asText();
            int totalElements = node.get("totalElements").asInt();
            int totalPages = node.get("totalPages").asInt();

            JsonNode contentNode = node.get("content");
            List<DottoreResponseDTO> contentList = mapper.readValue(contentNode.traverse(),
                    new TypeReference<List<DottoreResponseDTO>>() {});
            Page<DottoreResponseDTO> page = new PageImpl<>(contentList, PageRequest.of(pageNo, pageSize, Sort.by(sortBy)),
                    totalElements);

            return page;
        }

    }
}