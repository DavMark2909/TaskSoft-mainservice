package com.tasksoft.mark.mainservice.entity;
import com.tasksoft.mark.mainservice.entity.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    // 1. Make this nullable (it will be null if it's a group notification)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    // 2. Add the target group (it will be null if it's a direct user notification)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group targetGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    // 3. Replace boolean isRead with a JoinTable to track WHO read it
    @ManyToMany
    @JoinTable(
            name = "notification_reads",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> readByUsers = new HashSet<>();

    // Helper method to mark as read
    public void markAsReadByUser(User user) {
        this.readByUsers.add(user);
    }
}
