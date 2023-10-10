package dev.tripdraw.admin.presentation;

import dev.tripdraw.admin.application.AdminAuthService;
import dev.tripdraw.admin.application.AdminService;
import dev.tripdraw.admin.dto.AdminLoginRequest;
import dev.tripdraw.admin.dto.AdminPagingRequest;
import dev.tripdraw.admin.dto.AdminPostResponse;
import dev.tripdraw.admin.dto.AdminPostsResponse;
import dev.tripdraw.admin.dto.AdminTripResponse;
import dev.tripdraw.admin.dto.AdminTripsResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    private final AdminAuthService adminAuthService;

    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody AdminLoginRequest request, HttpServletRequest httpServletRequest) {
        String sessionId = adminAuthService.login(request);
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("SESSION", sessionId);
        session.setMaxInactiveInterval(3600);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/trips")
    public ResponseEntity<AdminTripsResponse> readTrips(AdminPagingRequest adminPagingRequest) {
        return ResponseEntity.ok(adminService.readTrips(adminPagingRequest.lastViewId(), adminPagingRequest.limit()));
    }

    @GetMapping("/trips/{tripId}")
    public ResponseEntity<AdminTripResponse> readTrips(@PathVariable("tripId") Long tripId) {
        return ResponseEntity.ok(adminService.readTripById(tripId));
    }

    @DeleteMapping("/trips/{tripId}")
    public ResponseEntity<Void> deleteTripById(@PathVariable("tripId") Long tripId) {
        adminService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/posts")
    public ResponseEntity<AdminPostsResponse> readPosts(AdminPagingRequest adminPagingRequest) {
        return ResponseEntity.ok(adminService.readPosts(adminPagingRequest.lastViewId(), adminPagingRequest.limit()));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<AdminPostResponse> readPosts(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(adminService.readPostById(postId));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable("postId") Long postId) {
        adminService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
