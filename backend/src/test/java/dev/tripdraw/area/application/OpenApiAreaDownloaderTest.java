package dev.tripdraw.area.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import dev.tripdraw.area.domain.Area;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;

@AutoConfigureWebClient(registerRestTemplate = true)
@RestClientTest(value = {OpenApiAreaDownloader.class})
class OpenApiAreaDownloaderTest {

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private OpenApiAreaDownloader openAPIAreaDownloader;

    @Test
    void 전체_행정구역을_받아온다() {
        // given
        String expectedAuthJson = "{\n"
                + "    \"id\": \"API_0101\",\n"
                + "    \"result\": {\n"
                + "        \"accessTimeout\": \"tripdraw\",\n"
                + "        \"accessToken\": \"tripdraw\"\n"
                + "    },\n"
                + "    \"errMsg\": \"Success\",\n"
                + "    \"errCd\": 0,\n"
                + "    \"trId\": \"M6KU_API_0101_1697571544657\"\n"
                + "}";
        mockServer.expect(requestTo("https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json"
                        + "?consumer_key=tripdraw&consumer_secret=tripdraw"
                ))
                .andExpect(method(GET))
                .andRespond(withSuccess(expectedAuthJson, APPLICATION_JSON));

        String expectedSeoulJson = "{\n"
                + "                              \"id\": \"API_0701\",\n"
                + "                              \"result\": [\n"
                + "                                {\n"
                + "                                  \"y_coor\": \"1944217\",\n"
                + "                                  \"full_addr\": \"서울특별시\",\n"
                + "                                  \"x_coor\": \"965142\",\n"
                + "                                  \"addr_name\": \"서울특별시\",\n"
                + "                                  \"cd\": \"11\"\n"
                + "                                }\n"
                + "                              ],\n"
                + "                              \"errMsg\": \"Success\",\n"
                + "                              \"errCd\": 0,\n"
                + "                              \"trId\": \"zvSI_API_0701_1496972620013\"\n"
                + "                            }";
        mockServer.expect(requestTo("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json"
                        + "?accessToken=tripdraw"))
                .andExpect(method(GET))
                .andRespond(withSuccess(expectedSeoulJson, APPLICATION_JSON));

        String expectedGangnamJson = "{\n"
                + "                              \"id\": \"API_0701\",\n"
                + "                              \"result\": [\n"
                + "                                {\n"
                + "                                  \"y_coor\": \"1944217\",\n"
                + "                                  \"full_addr\": \"서울특별시 강남구\",\n"
                + "                                  \"x_coor\": \"965142\",\n"
                + "                                  \"addr_name\": \"강남구\",\n"
                + "                                  \"cd\": \"11230\"\n"
                + "                                }\n"
                + "                              ],\n"
                + "                              \"errMsg\": \"Success\",\n"
                + "                              \"errCd\": 0,\n"
                + "                              \"trId\": \"zvSI_API_0701_1496972620013\"\n"
                + "                            }";
        mockServer.expect(requestTo("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json"
                        + "?accessToken=tripdraw&cd=11"))
                .andExpect(method(GET))
                .andRespond(withSuccess(expectedGangnamJson, APPLICATION_JSON));

        String expectedGangnamdongJson = "{\n"
                + "                              \"id\": \"API_0701\",\n"
                + "                              \"result\": [\n"
                + "                                {\n"
                + "                                  \"y_coor\": \"1944217\",\n"
                + "                                  \"full_addr\": \"서울특별시 강남구 강남동\",\n"
                + "                                  \"x_coor\": \"965142\",\n"
                + "                                  \"addr_name\": \"강남동\",\n"
                + "                                  \"cd\": \"11230111\"\n"
                + "                                }\n"
                + "                              ],\n"
                + "                              \"errMsg\": \"Success\",\n"
                + "                              \"errCd\": 0,\n"
                + "                              \"trId\": \"zvSI_API_0701_1496972620013\"\n"
                + "                            }";
        mockServer.expect(requestTo("https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json"
                        + "?accessToken=tripdraw&cd=11230"))
                .andExpect(method(GET))
                .andRespond(withSuccess(expectedGangnamdongJson, APPLICATION_JSON));

        // when
        List<Area> allAreas = openAPIAreaDownloader.download();

        // then
        assertThat(allAreas).containsExactly(new Area("서울특별시", "강남구", "강남동"));
    }
}
