package cat.udl.eps.softarch.demo.controller;

import cat.udl.eps.softarch.demo.domain.Business;
import cat.udl.eps.softarch.demo.repository.BusinessRepository;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController // Anotación especial para extender Spring Data REST
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessRepository businessRepository;

    public BusinessController(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    // RESTRINGIDO: Solo un ADMIN puede crear
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PersistentEntityResource> createBusiness(
            @RequestBody Business business,
            PersistentEntityResourceAssembler assembler) {

        Business savedBusiness = businessRepository.save(business);
        return ResponseEntity.created(assembler.toFullResource(savedBusiness).getLink("self").get().toUri())
                .body(assembler.toFullResource(savedBusiness));
    }

    // NOTA: Para GET, PUT, DELETE, si ya tienes un @RepositoryRestResource para Business,
    // Spring Data REST los genera automáticamente. Solo necesitarías sobreescribirlos
    // si quieres cambiar su comportamiento. Si no, esta es la forma de añadirlos.
}