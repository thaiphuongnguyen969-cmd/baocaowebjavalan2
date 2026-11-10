package com.fashion.backend.service.impl;

import com.fashion.backend.repository.DashboardRepository;
import com.fashion.backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final DashboardRepository repository;
    public DashboardServiceImpl(DashboardRepository repository) { this.repository=repository; }
    @Override public Map<String,Object> summary() { return repository.summary(); }
}
