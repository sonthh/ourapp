package com.son.controller;

import com.son.service.BarcodeService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Api(tags = "Barcode")
@RestController
@RequestMapping("/barcode")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeService barcodeService;

    @PostMapping(value = "/code128", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<BufferedImage> Code128Barcode(@RequestBody String barcode) throws Exception {
        return okResponse(barcodeService.generateCode128BarcodeImage(barcode));
    }

    private ResponseEntity<BufferedImage> okResponse(BufferedImage image) {
        return new ResponseEntity<>(image, HttpStatus.OK);
    }

    @GetMapping(value = "/generate")
    public Object test(ModelMap modelMap) throws Exception {
        String[] productData = new String[] {
          "so mi trang", "quan rin den", "ao khoac nam", "quan sip", "mu luoi trai", "ao thun tron", "ao da"
        };

        List<String> codes = new ArrayList<>();

        for (String item : productData) {
            BufferedImage bufferedImage = barcodeService.generateCode128BarcodeImage(item);

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", bao);
            byte[] bytes = bao.toByteArray();

            String base64 = Base64.getEncoder().encodeToString(bytes);
            String base4Url = "data:image/jpeg;base64," + base64;

            codes.add(base4Url);
        }

        modelMap.addAttribute("codes", codes);
        return new ModelAndView("barcode/generator");
    }
}