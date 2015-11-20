package com.theironyard.controllers;

import com.theironyard.entities.User;
import com.theironyard.services.UserRepository;
import com.theironyard.utils.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;


/**
 * Created by DrScott on 11/19/15.
 */
@RestController
public class FurrFaceController {
    @Autowired
    UserRepository users;

    @PostConstruct
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (users.count() == 0){
            User terry = new User();
            terry.username = "Terry";
            terry.password = PasswordHash.createHash("1234");
            terry.petName = "Maggie";
            terry.petRating = 0;
            terry.aboutMe = "Hi, I'm terry and I have a dog named Maggie!";
            terry.petType = "dog";
            terry.imageURL = "http://theartmad.com/wp-content/uploads/2015/03/Baby-Bunny-11.jpg";
            terry.petAge = 8;
            terry.neighborhood = "Charleston";
            users.save(terry);

            User doug = new User();
            doug.username = "Doug";
            doug.password = PasswordHash.createHash("1234");
            doug.petName = "Rowan";
            doug.petRating = 10;
            doug.aboutMe = "Hi, I'm Doug and I have a dog named Rowan!";
            doug.petType = "dog";
            doug.imageURL = "http://cp4s.laurietooker.com/wp-content/uploads/2011/04/shar-pei-Puppy.jpg";
            doug.petAge = 8;
            doug.neighborhood = "Charleston";
            users.save(doug);

            User kate = new User();
            kate.username = "Kate";
            kate.password = PasswordHash.createHash("1234");
            kate.petName = "Katedog";
            kate.petRating = 10;
            kate.aboutMe = "Hi, I'm Kate and I have a dog named katedog!";
            kate.petType = "dog";
            kate.imageURL = "http://cp4s.laurietooker.com/wp-content/uploads/2011/04/shar-pei-Puppy.jpg";
            kate.petAge = 8;
            kate.neighborhood = "Charleston";
            users.save(kate);

            User lindsay = new User();
            lindsay.username = "Lindsay";
            lindsay.password = PasswordHash.createHash("1234");
            lindsay.petName = "Lindsay cat";
            lindsay.petRating = 10;
            lindsay.aboutMe = "Hi, I'm Lindsay and I have a cat!!";
            lindsay.petType = "cat";
            lindsay.imageURL = "http://welovecatsandkittens.com/wp-content/uploads/2013/10/fluffy-kitten-ace.jpg";
            lindsay.petAge = 8;
            lindsay.neighborhood = "Charleston";
            users.save(lindsay);
        }

    }


    @RequestMapping("/addUser")
    public void addUser(HttpServletResponse response,
                      HttpSession session,
                      String username,
                      String password,
                      @RequestParam(defaultValue = "http://bit.ly/1I09WCO")String imageURL,
                      String petName,
                      @RequestParam(defaultValue = "unknown") String petType,
                      @RequestParam(defaultValue = "1") int petAge,
                      @RequestParam(defaultValue = "0")  int petRating,
                      @RequestParam(defaultValue = "User hasn't described themselves yet") String aboutMe,
                      @RequestParam(defaultValue = "westPhilly") String neighborhood) throws Exception {

            User user = new User();
            user.username = username;
            user.password = PasswordHash.createHash(password);
            user.imageURL = imageURL;
            user.petName = petName;
            user.petType = petType;
            user.petAge = petAge;
            user.neighborhood = neighborhood;
            user.aboutMe = aboutMe;
            user.petRating = petRating;
            users.save(user);

       // System.out.println("");
         session.setAttribute("username", username);
         response.sendRedirect("/");
    }
    @RequestMapping("/login")
    public void login(HttpSession session, HttpServletResponse response, String password) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);
        if (user == null){
            response.sendRedirect("/");
        } else if (!PasswordHash.validatePassword(password, user.password)) {
                throw new Exception("Wrong password");
               // response.sendRedirect("/");
            }
        }

    @RequestMapping("/logout")
    public void logout(HttpServletResponse response, HttpSession session) throws IOException {
        session.invalidate();
        response.sendRedirect("/");
        System.out.println("goodbye!");
    }
    @RequestMapping("/users")
    public List<User> users(){
        return (List<User>) users.findAll();
    }

    @RequestMapping("/currentUser")
    public User currentUser(HttpSession session){
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);
        return user;
    }

    @RequestMapping("/petType")
    public List<User> searchPetType (String petType){
        return users.findAllByPetType(petType);
    }
    @RequestMapping("/neighborhood")
    public List<User> searchByNeighborhood (String neighborhood){
        return users.findAllByNeighborhood( neighborhood);
    }

    @RequestMapping("/petAge")
    public List<User> searchByPetAge (int petAge){
        return users.findAllByPetAge(petAge);
    }

    /*@RequestMapping("/randomUser")
    public User randomUser(){
        return users.findRandomUser();
    }*/
   /* @RequestMapping("/ratings")
    public List<User> ratedUsers(){
        return users.findAllOrderByPetRatingAsc();
    }*/


}
