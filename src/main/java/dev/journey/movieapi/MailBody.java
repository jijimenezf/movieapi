package dev.journey.movieapi;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {
}
