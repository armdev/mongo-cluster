/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.project.app.pulse.pulse;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author armena
 */
@Data
@Document(collection = "pulse")
public class PulseModel implements Serializable{

    private static final long serialVersionUID = 1039246519725050770L;
    
    @Id
    private String id;
    
    private long pulse;

    public PulseModel(long pulse) {
        this.pulse = pulse;
    }
    
}
