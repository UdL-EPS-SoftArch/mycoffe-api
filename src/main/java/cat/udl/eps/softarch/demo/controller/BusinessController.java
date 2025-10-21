@RestController
@RequestMapping("/businesses")
public class BusinessController {

    private final BusinessRepository businessRepository;

    public BusinessController(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @GetMapping
    public List<Business> getAll() { ... }

    @PostMapping
    public Business create(@RequestBody Business business) { ... }

    @PutMapping("/{id}")
    public Business update(@PathVariable Long id, @RequestBody Business business) { ... }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { ... }
}
