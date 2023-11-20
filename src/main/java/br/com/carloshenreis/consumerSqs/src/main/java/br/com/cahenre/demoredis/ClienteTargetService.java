package br.com.cahenre.demoredis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteTargetService {

    @Autowired
    private RedisClient redisClient;

    public void someMethod() {
        String value = redisClient.getValue("116571416");
        System.out.println("Value: " + value);

        boolean isConnected = redisClient.ping();
        System.out.println("Is connected: " + isConnected);
    }

    public String getTargetDoCliente(String idCliente) {
        return redisClient.getValue(idCliente);
    }

}
