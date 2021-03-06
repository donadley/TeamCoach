package com.lsus.teamcoach.teamcoachapp.core;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * User service for connecting the the REST API and
 * getting the users.
 */
public interface UserService {

    @GET(Constants.Http.URL_USERS_FRAG)
    UsersWrapper getUsers();

    /**
     * The {@link retrofit.http.Query} values will be transform into query string paramters
     * via Retrofit
     *
     * @param email The users email
     * @param password The users password
     * @return A login response.
     */
    @GET(Constants.Http.URL_AUTH_FRAG)
    User authenticate(@Query(Constants.Http.PARAM_USERNAME) String email,
                               @Query(Constants.Http.PARAM_PASSWORD) String password);

    /**
     *
     * @param user The user
     * @return A register response.
     */
    @POST(Constants.Http.URL_USERS_FRAG)
    User register(@Body User user);

    /**
     * Request a password reset
     * @param email
     */
    @POST(Constants.Http.URL_PASSWORD_RESET_FRAG)
    Object passwordReset(@Body Email email);

    /**
     * update a user in the database
     *
     * @param user The user
     * @return A update response.
     */
    @PUT(Constants.Http.URL_USERS_FRAG+"/{id}")
    User update(@Path("id") String id, @Header("X-Parse-Session-Token") String token,  @Body User user);


    /**
     * Retrieves the current user from parse.com
     *
     * @param token The session token
     * @return the current user.
     */
    @GET(Constants.Http.URL_USERS_FRAG+"/me")
    User currentUser(@Header("X-Parse-Session-Token") String token);

    @GET(Constants.Http.URL_USERS_FRAG_CHILD +"/{objectID}")
    User currentUserWithChildren(@Path("objectID") String objectID);

    @GET(Constants.Http.URL_USERS_FRAG)
    UsersWrapper getTeamMembers(@Query("where") String constraint);
}