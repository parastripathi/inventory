package com.manager.inventory.service;

public interface InventoryService {

  void addInventory(String product, int quantity);

  void deductInventory(String product, int quantity);

  int getAvailableInventory(String product);

  void fulfillOrder(String product, int quantity);

  void setMaxInventoryLevel(String product, int maxInventory);
}
