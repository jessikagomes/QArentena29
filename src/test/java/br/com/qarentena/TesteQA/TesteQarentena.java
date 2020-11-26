package br.com.qarentena.TesteQA;

import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TesteQarentena {

    String idVotacao;
    @BeforeClass
    public static void base(){
        baseURI = "https://api.thecatapi.com/v1";
    }

   /* EXEMPLO
    @Test
    public void americanas(){
        given()
                .when().get("https://cep-v2-americanas-npf.b2w.io/cep/03102030")
                .then();

    }*/
   /* @Test
    public void cadastrarApi(){
        given()
                    .contentType("application/json")
                    .body("{\"email\":\"jessikagomes@hotmail.com.br\",\"appDescription\": \"teste jessika\"}")
                .when()
                    .post("https://api.thecatapi.com/v1/user/passwordlesssignup")
                .then()
                    .statusCode(200)
                .log().all();
    }*/

    @Test
    public void cadastrarApiCampoObrigatorioEmail(){
        given()
                .contentType("application/json")
                .body("{\"appDescription\": \"teste jessika\"}")
                .when()
                .post("/user/passwordlesssignup")
                .then()
                .log().all()
                .body("message",containsString("\"email\" is required"))
                .statusCode(400);
    }

    @Test
    public void votarApi(){
        given()
                    .contentType("application/json")
                    .body("{\"image_id\": \"4gmq_ixTj\", \"value\": true, \"sub_id\": \"demo-1a8d43\"}")
                    .header("x-api-key","7a948762-b666-4861-a652-221d0bf0687d")
                .when()
                    .post("/votes")
                .then()
                    .statusCode(200)
                    .body("message",is("SUCCESS"))
                    .log().all();
    }

    @Test
    public void pegarVotacaoComSucesso(){
        Response response =
        given()
                    .header("x-api-key","7a948762-b666-4861-a652-221d0bf0687d")
                .when()
                    .get("/votes");
                response.then()
                    .body("image_id",hasItems("4gmq_ixTj"))
                    .statusCode(200)
                    .log().all();

                idVotacao = response.jsonPath().getString("id[0]");
        System.out.println("ID RETORNADO: " + idVotacao);
    }

    @Test
    public void deletarVotacao(){
        votarApi();
        pegarVotacaoComSucesso();

        given()
                    .header("x-api-key","7a948762-b666-4861-a652-221d0bf0687d")
                    .contentType("application/json")
                    .pathParam("vote_id",idVotacao)
                .when()
                    .delete("/votes/{vote_id}")
                .then()
                    .statusCode(200)
                    .log().all();
    }
}
