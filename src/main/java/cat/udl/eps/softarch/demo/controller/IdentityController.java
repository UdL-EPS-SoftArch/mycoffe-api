package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Business;
import cat.udl.eps.softarch.demo.repository.BusinessRepository;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@BasePathAwareController
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessRepository businessRepository;

    public BusinessController(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    // Obtener un negocio por ID
    @GetMapping("/{id}")
    public @ResponseBody
    PersistentEntityResource getBusiness(
            @PathVariable Long id,
            PersistentEntityResourceAssembler resourceAssembler) {

        Optional<Business> business = businessRepository.findById(id);
        return business.map(resourceAssembler::toFullResource).orElse(null);
    }

    // Crear un negocio (solo ADMIN)
    @PostMapping
    public @ResponseBody
    PersistentEntityResource createBusiness(
            @RequestBody Business business,
            PersistentEntityResourceAssembler resourceAssembler) {

        Business savedBusiness = businessRepository.save(business);
        return resourceAssembler.toFullResource(savedBusiness);
    }

    // Actualizar un negocio (ADMIN o BUSINESS_ADMIN)
    @PutMapping("/{id}")
    public @ResponseBody
    PersistentEntityResource updateBusiness(
            @PathVariable Long id,
            @RequestBody Business updatedBusiness,
            PersistentEntityResourceAssembler resourceAssembler) {

        Optional<Business> existing = businessRepository.findById(id);
        if (existing.isPresent()) {
            Business business = existing.get();
            // Actualizar campos necesarios
            business.setName(updatedBusiness.getName());
            business.setStatus(updatedBusiness.getStatus());
            Business savedBusiness = businessRepository.save(business);
            return resourceAssembler.toFullResource(savedBusiness);
        }
        return null;
    }

    // Eliminar negocio (solo ADMIN)
    @DeleteMapping("/{id}")
    public void deleteBusiness(@PathVariable Long id) {
        businessRepository.deleteById(id);
    }
}
