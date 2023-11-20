package br.com.cahenre.demoredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteTargetService clienteService;

    @GetMapping("/{idCliente}/target")
    public ResponseEntity<String> getTargetDoCliente(@PathVariable String idCliente) {
        clienteService.someMethod();
        String target = clienteService.getTargetDoCliente(idCliente);

        if (target != null) {
            return ResponseEntity.ok(target);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

