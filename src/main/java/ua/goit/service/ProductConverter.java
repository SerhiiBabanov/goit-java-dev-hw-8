package ua.goit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.goit.model.dao.ProducerDAO;
import ua.goit.model.dao.ProductDAO;
import ua.goit.model.dto.ProductDTO;
import ua.goit.repository.ProducerRepository;

@Service
@RequiredArgsConstructor
public class ProductConverter {
    private final ProducerConverter producerConverter;
    private final ProducerRepository producerRepository;

    public ProductDTO mapToDTO(final ProductDAO productDAO) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productDAO.getId());
        productDTO.setName(productDAO.getName());
        productDTO.setPrice(productDAO.getPrice());
        productDTO.setProducer(producerConverter.mapToDTO(productDAO.getProducer()));
        return productDTO;
    }

    public ProductDAO mapToDAO(final ProductDTO productDTO) {
        ProductDAO productDAO = new ProductDAO();
        productDAO.setName(productDTO.getName());
        productDAO.setPrice(productDTO.getPrice());
        final ProducerDAO producerDAO = producerRepository.findById(productDTO.getProducer().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "producer not found"));
        productDAO.setProducer(producerDAO);
        return productDAO;
    }
}
