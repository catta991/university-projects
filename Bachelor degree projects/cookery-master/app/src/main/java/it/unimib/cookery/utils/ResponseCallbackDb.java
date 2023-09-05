package it.unimib.cookery.utils;
/**
 * Interface to send data from Repositories to Activity/Fragment.
 */
public interface ResponseCallbackDb <T>{
    void onResponse(T obj);
    void onResponsePantry(T obj);
    void onResponseSearchIngredient(T obj);
    void onFailure(String errorMessage);
}
