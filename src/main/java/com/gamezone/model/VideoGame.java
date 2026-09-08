
package com.gamezone.model;




/**
 * Represents a video game sold by the GameZone store. A video game includes its
 * platform, genre, and age rating.
 */
public class VideoGame extends Product {
    
    private String platform; 
    private String genre; 
    private String ageRating; 

   
    /**
     * Creates a video game with its common product information and video
     * game-specific characteristics.
     *
     * @param id the unique identifier of the video game
     * @param title the title of the video game
     * @param price the price of the video game
     * @param stock the available quantity in inventory
     * @param platform the platform on which the video game is played
     * @param genre the genre of the video game
     * @param ageRating the recommended age rating of the video game
     */
    public VideoGame(String id, String title, double price, int stock, String platform, String genre, String ageRating) {
        super(id, title, price, stock);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }
    
    /**
     * Returns the platform of the video game.
     *
     * @return the video game platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * Sets the platform of the video game.
     *
     * @param platform the new video game platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * Returns the genre of the video game.
     *
     * @return the video game genre
     */
    public String getGenre() {
        return genre;
    }
    
    /**
     * Sets the genre of the video game.
     *
     * @param genre the new video game genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    /**
     * Returns the age rating of the video game.
     *
     * @return the video game age rating
     */
    public String getAgeRating() {
        return ageRating;
    }
    
    /**
     * Sets the age rating of the video game.
     *
     * @param ageRating the new video game age rating
     */
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    /**
     * Returns a complete description of the video game.
     *
     * @return a string containing the video game information
     */
    @Override
    public  String getDescription(){
         return "Title: " + getTitle()
                + ", Platform: " + platform
                + ", Genre: " + genre
                + ", Age rating: " + ageRating; 
                
    }
}
