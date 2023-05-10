package com.example.consumer.services;

import com.example.consumer.repository.TrapRepository;
import com.example.consumer.entity.Trap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TrapService {
    @Autowired
    private TrapRepository trapRepository;

    private final Logger log = LoggerFactory.getLogger(TrapService.class);
    public Iterable<Trap> getTraps() {

        try{
            return trapRepository.findAll();
        } catch (Exception e) {
            log.error("Error getting data from Elastic", e);
            return null;
        }
    }

    public Trap insertTrap(Trap  trap) {
      try {
          trapRepository.save(trap);

          log.info("Data saved successfully in Elastic");
          return trap;
      }
      catch (Exception e) {
          log.error("Error saving data in Elastic", e);
      }
      return null;
    }

    public Trap updateTrap(Trap trap, int id) {
        Trap trap1  = trapRepository.findById(id).get();
        trap1.setTrap(trap.getTrap());
        return trap1;
    }

    public void deleteTrap(int id ) {
        trapRepository.deleteById(id);
    }
    public void saveKafkaMessageToElastic(String message) {
        try {
            Trap trap = new Trap(message);
            trapRepository.save(trap);

            log.info("Data saved successfully in Elastic");

        } catch (Exception e) {

            System.out.println("error " + e);
        }
    }
}
