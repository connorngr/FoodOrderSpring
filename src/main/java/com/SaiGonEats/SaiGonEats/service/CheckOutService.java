    package com.SaiGonEats.SaiGonEats.service;

import com.SaiGonEats.SaiGonEats.config.VNPayConfig;
import com.SaiGonEats.SaiGonEats.model.*;
import com.SaiGonEats.SaiGonEats.repository.IUserRepository;
import com.SaiGonEats.SaiGonEats.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

    @Service
    @RequiredArgsConstructor
    public class CheckOutService {

        private final OrderRepository orderRepository;
        private final IUserRepository iUserRepository;

        //
        @Transactional
        public Order checkOut(String nameCustomer) {

            Optional<User> optionalUser = iUserRepository.findByUsername(nameCustomer);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                ShoppingCart cart = user.getShoppingCart();
                if (cart != null && !cart.getItems().isEmpty()) {
                    Order order = new Order();
                    order.setUser(user);
                    order.setOrderDate(LocalDateTime.now());
                    order.setStatus("PENDING");
                    order.setAddress(user.getAddress());
                    order.setPhone(user.getPhone());

                    int totalAmount = 0;
                    for (ShoppingCartItem cartItem : cart.getItems()) {
                        OrderDetail orderItem = new OrderDetail();
                        orderItem.setOrder(order);
                        orderItem.setMenuItem(cartItem.getMenuItem());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPrice(cartItem.getPrice());
                        totalAmount += cartItem.getQuantity() * cartItem.getPrice();
                        order.getOrderDetails().add(orderItem);
                    }
                    cart.clearItems();
                    order.setTotalAmount(totalAmount);
                    orderRepository.save(order);
                    return order;
                }
            }
            return null;
        }

        @Transactional
        public String checkOutWithPayOnline(String nameCustomer, String urlReturn) {

            Optional<User> optionalUser = iUserRepository.findByUsername(nameCustomer);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                ShoppingCart cart = user.getShoppingCart();
                if (cart != null && !cart.getItems().isEmpty()) {
                    Order order = new Order();
                    order.setUser(user);
                    order.setOrderDate(LocalDateTime.now());
                    order.setStatus("PENDING");
                    order.setAddress(user.getAddress());
                    order.setPhone(user.getPhone());

                    int totalAmount = 0;
                    for (ShoppingCartItem cartItem : cart.getItems()) {
                        OrderDetail orderItem = new OrderDetail();
                        orderItem.setOrder(order);
                        orderItem.setMenuItem(cartItem.getMenuItem());
                        orderItem.setQuantity(cartItem.getQuantity());
                        orderItem.setPrice(cartItem.getPrice());
                        totalAmount += cartItem.getQuantity() * cartItem.getPrice();
                        order.getOrderDetails().add(orderItem);
                    }
                    cart.clearItems();
                    System.out.println("Price: " + totalAmount);
                    order.setTotalAmount(totalAmount);
                    orderRepository.save(order);
                    String paymentUrl = createOrder(order , urlReturn);
                    return paymentUrl;

                }
            }
            return null;
        }

        public static String createOrder(Order orders, String urlReturn) {
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
    //      Id Transaction
            String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
            String vnp_IpAddr = "127.0.0.1";
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            String orderType = "order-type";


    //        Put data in VNP
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
    //       Type PayVN
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    //       Value Amount
            vnp_Params.put("vnp_Amount", String.valueOf(orders.getTotalAmount()*100));
            vnp_Params.put("vnp_CurrCode", "VND");

            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            // customer information
            vnp_Params.put("vnp_OrderInfo", orders.getUser().getUsername() + " " + orders.getUser().getAddress());
            vnp_Params.put("vnp_OrderType", orderType);

            String locate = "vn";
            vnp_Params.put("vnp_Locale", locate);
    //---------------------------------------
            urlReturn += VNPayConfig.vnp_Returnurl;
            vnp_Params.put("vnp_ReturnUrl", urlReturn);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
    //----------------------------------------


            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    try {
                        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                        //Build query
                        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                        query.append('=');
                        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

            return paymentUrl;
        }
        public int orderReturn(HttpServletRequest request) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = null;
                String fieldValue = null;
                try {
                    fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                    fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            String signValue = VNPayConfig.hashAllFields(fields);
            if (signValue.equals(vnp_SecureHash)) {
                if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return -1;
            }
        }

    }