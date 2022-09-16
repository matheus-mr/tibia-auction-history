package com.matheusmr.tibiaauctionhistory.auctionfetch.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaginatedContentResponse(@JsonProperty("AjaxObjects") List<AjaxObject> ajaxObjects) {}
