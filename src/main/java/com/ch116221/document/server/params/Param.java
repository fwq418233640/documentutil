package com.ch116221.document.server.params;

import com.ch116221.document.server.domains.ConnectionInstance;
import lombok.Data;

import java.util.List;

@Data
public class Param {

    private List<String> tables;

    private ConnectionInstance instance;
}
