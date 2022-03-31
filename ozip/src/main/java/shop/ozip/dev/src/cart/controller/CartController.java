package shop.ozip.dev.src.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.ozip.dev.config.BaseException;
import shop.ozip.dev.config.BaseResponse;
import shop.ozip.dev.src.cart.model.PostCartReq;
import shop.ozip.dev.src.cart.model.PostCartRes;
import shop.ozip.dev.src.cart.provider.CartProvider;
import shop.ozip.dev.src.cart.service.CartService;
import shop.ozip.dev.utils.JwtService;

@RequiredArgsConstructor
@ResponseBody
@RestController
@RequestMapping("/app/stores")
public class CartController {

    private final CartProvider cartProvider;
    private final CartService cartService;

    @PostMapping("/products/{productId}/cart")
    public BaseResponse<PostCartRes> insert(@PathVariable Long productId, @RequestBody PostCartReq postCartReq) {
        postCartReq.setProductId(productId);

        try {
            Long cartId = cartService.insert(postCartReq);
            return new BaseResponse<>(new PostCartRes(cartId));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}
