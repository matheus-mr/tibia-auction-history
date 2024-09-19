package com.tibiaauctionhistory.service.auctionfetch.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PaginatedContentResponse(@JsonProperty("AjaxObjects") List<AjaxObject> ajaxObjects) {}
