package com.semitop7.resolver;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.semitop7.db.entity.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientDeserializer extends StdDeserializer<Client> {
    private Map<Long, Client> items;

    public ClientDeserializer() {
        this(null);
    }

    public ClientDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Client deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        Client client = mapper.readValue(p, Client.class);
        if (client == null) {
            return null;
        }
        if (items == null) {
            items = new HashMap<>();
        }
        if (!items.containsKey(client.getInn())) {
            items.put(client.getInn(), client);
        } else {
            return items.get(client.getInn());
        }
        return client;
    }
}
