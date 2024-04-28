package com.manager.inventory.service.impl;

import com.manager.inventory.exception.InsufficientInventoryException;
import com.manager.inventory.exception.InvalidInventoryTransactionException;
import com.manager.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InventoryServiceImpl implements InventoryService {

  private final Map<String, Integer> inventory;
  private final Map<String, Integer> maxInventoryLevels;

  @Value("${max.inventory.default}")
  private int maxInventoryDefault;

  public InventoryServiceImpl() {
    this.inventory = new ConcurrentHashMap<>();
    this.maxInventoryLevels = new ConcurrentHashMap<>();
  }

  @Override
  public void addInventory(String product, int quantity) {
    if (quantity <= 0) {
      throw new InvalidInventoryTransactionException(
          "Quantity must be greater than 0 for adding inventory.");
    }
    int currentQuantity = inventory.getOrDefault(product, 0);
    int maxInventory = maxInventoryLevels.getOrDefault(product, Integer.MAX_VALUE);
    if (currentQuantity + quantity > maxInventory) {
      throw new InvalidInventoryTransactionException(
          "Adding inventory exceeds maximum inventory level for product: " + product);
    }
    inventory.put(product, currentQuantity + quantity);
  }

  @Override
  public void deductInventory(String product, int quantity) {
    if (quantity <= 0) {
      throw new InvalidInventoryTransactionException(
          "Quantity must be greater than 0 for deducting inventory.");
    }
    int currentQuantity = inventory.getOrDefault(product, 0);
    if (currentQuantity < quantity) {
      throw new InsufficientInventoryException("Insufficient inventory for product: " + product);
    }
    inventory.put(product, currentQuantity - quantity);
  }

  @Override
  public int getAvailableInventory(String product) {
    return inventory.getOrDefault(product, 0);
  }

  @Override
  public void fulfillOrder(String product, int quantity) {
    if (quantity <= 0) {
      throw new InvalidInventoryTransactionException(
          "Quantity must be greater than 0 for fulfilling orders.");
    }
    int currentQuantity = inventory.getOrDefault(product, 0);
    if (currentQuantity < quantity) {
      throw new InsufficientInventoryException("Insufficient inventory for product: " + product);
    }
    inventory.put(product, currentQuantity - quantity);
  }

  @Override
  public void setMaxInventoryLevel(String product, int maxInventory) {
    if (maxInventory <= 0) {
      throw new InvalidInventoryTransactionException(
          "Maximum inventory level must be greater than 0.");
    }
    maxInventoryLevels.put(product, maxInventory);
  }
}
