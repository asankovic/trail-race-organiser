package com.asankovic.race.command.controllers;

import com.asankovic.race.command.data.dtos.rest.CreateRunnerData;
import com.asankovic.race.command.data.dtos.rest.UpdateRunnerData;
import com.asankovic.race.command.filters.JwtFilter;
import com.asankovic.race.command.handlers.GlobalExceptionHandler;
import com.asankovic.race.command.services.RunnerMessageService;
import com.asankovic.race.command.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ContextConfiguration(classes = {JwtFilter.class, JwtUtil.class, RunnerMutatingController.class, GlobalExceptionHandler.class})
@TestPropertySource(properties = {"jwt.secret=dGhpcyBpcyBhIHNlY3JldCBrZXkgY3JlYXRlZCB3aXRoIHJYWtpuX5lNCbSb1hdTzxtZRBY50Bhs32jxZg0D0E6DQd0oEx6lg"})
@WebMvcTest(RunnerMutatingController.class)
class RunnerMutatingControllerTest {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RunnerMessageService runnerMessageService;

    @Test
    void shouldCreateRunner_whenValidRequest() throws Exception {
        final var createData = new CreateRunnerData("firstName", "lastName", null, "5k");

        mockMvc.perform(post(RunnerMutatingController.ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createValidJWTToken())
                        .content(objectMapper.writeValueAsBytes(createData)))
                .andExpect(status().isOk());

        verify(runnerMessageService).publishCreationEvent(any(CreateRunnerData.class));
    }

    @Test
    void shouldReturnUnauthorized_whenRequestingACreateWithExpiredToken() throws Exception {
        mockMvc.perform(post(RunnerMutatingController.ENDPOINT)
                        .header("Authorization", "Bearer " + createExpiredJWTToken()))
                .andExpect(status().isUnauthorized());

        verify(runnerMessageService, never()).publishCreationEvent(any(CreateRunnerData.class));
    }

    @Test
    void shouldReturnValidationError_whenCreationDataIsNotValid() throws Exception {
        final var createData = new CreateRunnerData(SPACE, SPACE, null, SPACE);

        mockMvc.perform(post(RunnerMutatingController.ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createValidJWTToken())
                        .content(objectMapper.writeValueAsBytes(createData)))
                .andExpect(status().isBadRequest());

        verify(runnerMessageService, never()).publishCreationEvent(any(CreateRunnerData.class));
    }

    @Test
    void shouldUpdateRunner_whenValidRequest() throws Exception {
        final var updateData = new UpdateRunnerData("firstName", null, null, null);

        mockMvc.perform(patch(RunnerMutatingController.ENDPOINT + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createValidJWTToken())
                        .content(objectMapper.writeValueAsBytes(updateData)))
                .andExpect(status().isOk());

        verify(runnerMessageService).publishUpdateEvent(any(UpdateRunnerData.class), anyString());
    }

    @Test
    void shouldReturnUnauthorized_whenRequestingAnUpdateWithExpiredToken() throws Exception {
        mockMvc.perform(patch(RunnerMutatingController.ENDPOINT + "/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + createExpiredJWTToken()))
                .andExpect(status().isUnauthorized());

        verify(runnerMessageService, never()).publishUpdateEvent(any(UpdateRunnerData.class), anyString());
    }

    @Test
    void shouldReturnValidationError_whenUpdateDataIsNotValid() throws Exception {
        final String invalidName = "%test$"; // not allowed characters
        final var updateData = new UpdateRunnerData(invalidName, null, null, null);

        mockMvc.perform(patch(RunnerMutatingController.ENDPOINT + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createValidJWTToken())
                        .content(objectMapper.writeValueAsBytes(updateData)))
                .andExpect(status().isBadRequest());

        verify(runnerMessageService, never()).publishUpdateEvent(any(UpdateRunnerData.class), anyString());
    }

    @Test
    void shouldReturnBadRequest_whenUpdatingResourceWithPublicIDThatIsNotUUID() throws Exception {
        final var updateData = new UpdateRunnerData("firstName", null, null, null);

        mockMvc.perform(patch(RunnerMutatingController.ENDPOINT + "/notUUIDString")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + createValidJWTToken())
                        .content(objectMapper.writeValueAsBytes(updateData)))
                .andExpect(status().isBadRequest());

        verify(runnerMessageService, never()).publishUpdateEvent(any(UpdateRunnerData.class), anyString());
    }

    @Test
    void shouldDeleteRunner_whenValidRequest() throws Exception {
        mockMvc.perform(delete(RunnerMutatingController.ENDPOINT + "/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + createValidJWTToken()))
                .andExpect(status().isOk());

        verify(runnerMessageService).publishDeletionEvent(anyString());
    }

    @Test
    void shouldReturnUnauthorized_whenRequestingDeletionWithExpiredToken() throws Exception {
        mockMvc.perform(delete(RunnerMutatingController.ENDPOINT + "/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + createExpiredJWTToken()))
                .andExpect(status().isUnauthorized());

        verify(runnerMessageService, never()).publishDeletionEvent(anyString());
    }

    @Test
    void shouldReturnBadRequest_whenDeletingResourceWithPublicIDThatIsNotUUID() throws Exception {
        mockMvc.perform(delete(RunnerMutatingController.ENDPOINT + "/notUUIDString")
                        .header("Authorization", "Bearer " + createValidJWTToken()))
                .andExpect(status().isBadRequest());

        verify(runnerMessageService, never()).publishUpdateEvent(any(UpdateRunnerData.class), anyString());
    }

    private String createValidJWTToken() {
        final Date expirationDate = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return createJWTToken(expirationDate);
    }

    private String createExpiredJWTToken() {
        final Date expirationDate = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return createJWTToken(expirationDate);
    }

    private String createJWTToken(final Date expirationDate) {
        return Jwts.builder()
                .expiration(expirationDate)
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}