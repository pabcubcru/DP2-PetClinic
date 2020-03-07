package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shops/1")
public class ProductController {

	private ProductService productService;
	private ShopService shopService;
	
	@Autowired
	public ProductController(ProductService productService, ShopService shopService) {
		this.productService = productService;
		this.shopService = shopService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/products/new")
	public String initNewProductForm(Map<String, Object> model) {
		Product product = new Product();
		model.put("product", product);
		return "products/createOrUpdateProductForm";
	}
	
	@PostMapping(value = "/products/new")
	public String processNewProductForm(@Valid Product product, BindingResult result) {
		if (result.hasErrors()) {
			return "products/createOrUpdateProductForm";
		}
		else {
			Shop shop = this.shopService.findShops().iterator().next();
			product.setShop(shop);
			this.productService.saveProduct(product);
			shop.addProduct(product);
			return "redirect:/shops/1/products/" + product.getId();
		}
	}
	
	@GetMapping("/products/{productId}")
	public ModelAndView showOrder(@PathVariable("productId") int productId) {
		ModelAndView mav = new ModelAndView("products/productDetails");
		Product product = this.productService.findProductById(productId);
		mav.addObject(product);
		return mav;
	}
}
