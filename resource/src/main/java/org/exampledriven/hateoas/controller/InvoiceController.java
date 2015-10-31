package org.exampledriven.hateoas.controller;

import org.exampledriven.hateoas.domain.Invoice;
import org.exampledriven.hateoas.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api", produces = "application/hal+json")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/invoice", method = RequestMethod.GET)
    public Resources<Resource<Invoice>> getInvoiceByCustomerId(@RequestParam("customerId") int customerId) {

        return invoiceToResource(invoiceService.getInvoiceByCustomerId(customerId), customerId);

    }

    @RequestMapping(value = "/invoice/{id}", method = RequestMethod.GET)
    public Resource<Invoice> getInvoiceById(@PathVariable int id) {

        Invoice invoice = invoiceService.getInvoiceById(id);
        return invoiceToResource(invoice);

    }

    private Resources<Resource<Invoice>> invoiceToResource(List<Invoice> invoices, int customerId) {

        Link selfLink = linkTo(methodOn(InvoiceController.class).getInvoiceByCustomerId(customerId)).withSelfRel();

        List<Resource<Invoice>> invoiceResources = invoices.stream().map(InvoiceController::invoiceToResource).collect(Collectors.toList());

        return new Resources<>(invoiceResources, selfLink);

    }

    private static Resource<Invoice> invoiceToResource(Invoice invoice) {
        Link selfLink = linkTo(methodOn(InvoiceController.class).getInvoiceById(invoice.getId())).withSelfRel();

        return new Resource<>(invoice, selfLink);
    }
}