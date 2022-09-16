package com.matheusmr.tibiaauctionhistory.auctionfetch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AjaxObject(@JsonProperty("Data") String data) {}
