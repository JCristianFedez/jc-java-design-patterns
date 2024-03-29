package com.cristian.design.patterns.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageData extends AbstractDataType {

  private final String message;

  public static DataType of(final String message) {
    return new MessageData(message);
  }
}
