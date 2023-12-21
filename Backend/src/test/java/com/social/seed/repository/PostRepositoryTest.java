package com.social.seed.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataNeo4jTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Before
    public void setUp(){
        // clean all
        postRepository.deleteAll();

        // Adding test data
        // post #1
        // post #2
        // post #3

    }

    @After
    public void tearDown(){ postRepository.deleteAll();}

}
