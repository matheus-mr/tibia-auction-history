package com.tibiaauctionhistory.service.auctionfetch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AjaxObject(@JsonProperty("Data") String data) {}
