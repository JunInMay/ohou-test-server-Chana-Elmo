package shop.ozip.dev.src.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.ozip.dev.src.cart.entity.Cart;
import shop.ozip.dev.src.cart.entity.CartItem;
import shop.ozip.dev.src.cart.model.PostCartReq;
import shop.ozip.dev.src.cart.repository.CartItemRepository;
import shop.ozip.dev.src.product.entity.Product;
import shop.ozip.dev.src.product.option.entity.MachineOption;
import shop.ozip.dev.src.product.option.provider.OptionProvider;
import shop.ozip.dev.src.product.provider.ProductProvider;

@RequiredArgsConstructor
@Service
@ResponseBody
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductProvider productProvider;
    private final OptionProvider optionProvider;

    public CartItem createItem(Cart cart, PostCartReq postCartReq) {
        Product product = productProvider.getProduct(postCartReq.getProductId());
        MachineOption option = optionProvider.getOption(postCartReq.getOptionId());

        CartItem cartItem = cartItemRepository.save(CartItem.builder()
                .productId(product)
                .optionId(option)
                .count(postCartReq.getProductCount())
                .totalPrice(postCartReq.getPrice())
                .deliveryAmount(postCartReq.getDeliveryAmount())
                .cartId(cart)
                .build());

        return cartItem;
    }
}
