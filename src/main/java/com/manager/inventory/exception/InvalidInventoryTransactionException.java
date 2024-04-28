package com.manager.inventory.exception;

public class InvalidInventoryTransactionException extends RuntimeException {
  public InvalidInventoryTransactionException(String message) {
    super(message);
  }
}
