/*
 *  Copyright (c) 2018 Jens Eliasson, Luleå University of Technology
 *
 *  This work is part of the Productive 4.0 innovation project, which receives grants from the
 *  European Commissions H2020 research and innovation programme, ECSEL Joint Undertaking
 *  (project no. 737459), the free state of Saxony, the German Federal Ministry of Education and
 *  national funding authorities from involved countries.
 */

package eu.arrowhead.common.messages;

import java.util.Vector;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class SenMLMessage{


  //Vector<Record> e;
  @Valid
  @NotNull
  private String bn;

  private Double bt;
  private String bu;
  private Double bv;
  private Double bs;
  private Short bver;
  private String n;

  public SenMLMessage() {

  }

  public void setBn(String bn) {
    this.bn = bn;
  }

  public String getBn() {
    return bn;
  }

  public void setBt(Double bt) {
    this.bt = bt;
  }

  public Double getBt() {
    return bt;
  }

  public void setBu(String bu) {
    this.bu = bu;
  }

  public String getBu() {
    return bu;
  }

  public class Record {
    private String bn;
    private Double bt;
    private String bu;
    private Double bv;
    private Double bs;
    private Short bver;
    private String n;

    public Record() {
      bn = "test";
      bt = 0.0;
      bver = 5;
    }

    public String getN() {
      return n;
    }
    public void setN(String n) {
      this.n = n;
    }

  }


}
