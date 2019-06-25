package com.jason.ad.service;


import com.jason.ad.Application;
import com.jason.ad.exception.AdException;
import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AdUnitServiceTest {
    @Autowired
    private IAdUnitService unitService;

    @Test
    public void testGetAdUnitService() throws AdException {

    }
}
