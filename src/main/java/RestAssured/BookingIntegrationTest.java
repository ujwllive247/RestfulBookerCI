package RestAssured;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingIntegrationTest {

    public static void main(String[] args) {

        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // 1. This block of code , generate the token .......
        String authPayload = "{\n" +
                "  \"username\": \"admin\",\n" +
                "  \"password\": \"password123\"\n" +
                "}";

        Response authResponse = given()
                .contentType("application/json")
                .body(authPayload)
                .post("/auth");

        String token = authResponse.jsonPath().getString("token");
        System.out.println("✅ Token: " + token);

        // 2. Create Booking - defining the payload.  .
        String createPayload = "{\n" +
                "  \"firstname\": \"Ujjwal\",\n" +
                "  \"lastname\": \"Kumar\",\n" +
                "  \"totalprice\": 111,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2024-07-01\",\n" +
                "    \"checkout\": \"2024-07-10\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";

        Response createResponse = given()
                .contentType("application/json")
                .body(createPayload)
                .post("/booking");

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        System.out.println("✅ Booking created with ID: " + bookingId);

        // 3. Get Booking
        Response getResponse = given()
                .get("/booking/" + bookingId);

        System.out.println("📦 Booking Details:\n" + getResponse.asPrettyString());

        // 4. Update Booking (partial update)
        String updatePayload = "{\n" +
                "  \"firstname\": \"Ujjwal\",\n" +
                "  \"lastname\": \"Thakur\"\n" +
                "}";

        Response updateResponse = given()
                .contentType("application/json")
                .cookie("token", token)
                .body(updatePayload)
                .patch("/booking/" + bookingId);

        System.out.println("🛠️ Updated Booking:\n" + updateResponse.asPrettyString());

        // 5. Delete Booking
        Response deleteResponse = given()
                .contentType("application/json")
                .cookie("token", token)
                .delete("/booking/" + bookingId);

        System.out.println("❌ Delete Status: " + deleteResponse.getStatusCode());

        // 6. Try to Get Deleted Booking
        Response getAfterDelete = given()
                .get("/booking/" + bookingId);

        System.out.println("🔍 Get After Delete Status: " + getAfterDelete.getStatusCode());
        System.out.println("Get After Delete Response: " + getAfterDelete.asString());

        // Updated code
    }
}