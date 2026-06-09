package com.TipTop.model;

import java.util.Optional;

public record CheckAttempt(boolean allowed, Optional<Double> remainningTokens) {

}
