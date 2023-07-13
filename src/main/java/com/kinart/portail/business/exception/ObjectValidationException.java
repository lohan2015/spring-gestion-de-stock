package com.kinart.portail.business.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * @author cyrille.mbassi
 * @since 15.09.22
 */

@RequiredArgsConstructor
public class ObjectValidationException extends RuntimeException {

  @Getter
  private final Set<String> violations;

  @Getter
  private final String violationSource;

}
