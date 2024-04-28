package com.manager.inventory.controller;

import com.manager.inventory.model.InventoryRequest;
import com.manager.inventory.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
@Api(tags = "Inventory Management", description = "APIs for managing inventory levels")
public class InventoryController {

  @Autowired
  private InventoryService inventoryService;

  @PostMapping("/add")
  @ApiOperation(value = "Add inventory for a product", notes = "Adds the specified quantity of a "
      + "product to the inventory.")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Inventory added successfully"),
      @ApiResponse(code = 400, message = "Invalid request format or product not found")})
  public ResponseEntity<Void> addInventory(@RequestBody InventoryRequest request) {
    if (request.getQuantity() <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    inventoryService.addInventory(request.getProduct(), request.getQuantity());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/deduct")
  @ApiOperation(value = "Deduct inventory for a product", notes = "Deducts the specified quantity"
      + " of a product from the inventory.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Inventory deducted successfully"),
      @ApiResponse(code = 400, message = "Invalid request format, insufficient inventory, or "
          + "product not found")})
  public ResponseEntity<Void> deductInventory(@RequestBody InventoryRequest request) {
    if (request.getQuantity() <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (inventoryService.getAvailableInventory(request.getProduct()) < request.getQuantity()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    inventoryService.deductInventory(request.getProduct(), request.getQuantity());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @GetMapping("/{product}")
  @ApiOperation(value = "Get available inventory for a product", notes = "Retrieves the available"
      + " inventory quantity for the specified product.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval"),
      @ApiResponse(code = 404, message = "Product not found")})
  public ResponseEntity<Integer> getAvailableInventory(@PathVariable String product) {
    int availableInventory = inventoryService.getAvailableInventory(product);
    return ResponseEntity.ok(availableInventory);
  }

  @PostMapping("/fulfill")
  @ApiOperation(value = "Fulfill an order by deducting inventory", notes = "Deducts the inventory"
      + " when fulfilling an order.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Order fulfilled successfully"),
      @ApiResponse(code = 400, message = "Invalid request format, insufficient inventory, or "
          + "product not found")})
  public ResponseEntity<Void> fulfillOrder(@RequestBody InventoryRequest request) {
    if (request.getQuantity() <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    if (inventoryService.getAvailableInventory(request.getProduct()) < request.getQuantity()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    inventoryService.fulfillOrder(request.getProduct(), request.getQuantity());
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @PostMapping("/{product}/maxInventory")
  @ApiOperation(value = "Set maximum inventory level for a product", notes = "Sets the maximum "
      + "inventory level for the specified product.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Maximum inventory level set successfully"),
      @ApiResponse(code = 400, message = "Invalid request format or product not found")})
  public ResponseEntity<Void> setMaxInventoryLevel(@PathVariable String product,
      @RequestParam int maxInventory) {
    if (maxInventory <= 0) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    inventoryService.setMaxInventoryLevel(product, maxInventory);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}


