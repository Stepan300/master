package main.model;

import javax.persistence.*;

@Entity
public class Executor
{
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
    private int executorId;
    private String name;
    private String position;
    // ======================================================

    public int getExecutorId() {return executorId;}
    public void setExecutorId(int id) {this.executorId = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getRandomName() {return NameExecutorType.getRandomNameType();}

    public String getPosition() {return position;}
    public void setPosition(String position) {this.position = position;}
    public String getRandomPosition() {return PositionExecutorType.getRandomPositionType();}
}
