package com.wikigroup.controllers;

import com.wikigroup.models.Contacto;
import com.wikigroup.repositories.ContactoRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;
import java.util.List;

@Controller
public class ContactoController {

    private final ContactoRepository repo;

    public ContactoController(ContactoRepository repo) {
        this.repo = repo;
    }

    // --- Mostrar formulario de contacto ---
    @GetMapping("/contacto")
    public String form(Model model) {
        model.addAttribute("pageTitle", "Contáctenos");
        if (!model.containsAttribute("contacto")) {
            model.addAttribute("contacto", new Contacto());
        }
        return "contacto";
    }

    // --- Recibir envío del formulario ---
    @PostMapping("/contacto")
    public String submit(@Valid @ModelAttribute("contacto") Contacto contacto,
                         BindingResult br,
                         RedirectAttributes ra) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.contacto", br);
            ra.addFlashAttribute("contacto", contacto);
            ra.addFlashAttribute("errorMsg", "Revisa los campos en rojo.");
            return "redirect:/contacto";
        }
        repo.save(contacto);
        ra.addFlashAttribute("okMsg", "¡Mensaje enviado correctamente!");
        return "redirect:/contacto";
    }

    // --- Listado simple (opcional) ---
    @GetMapping("/contacto/admin")
    public String admin(Model model) {
        model.addAttribute("pageTitle", "Contactos");
        model.addAttribute("items", repo.findAll());
        return "contacto_admin";
    }

    // --- Listado ordenado por fecha desc ---
    @GetMapping("/mensajes")
    public String mensajes(Model model, @RequestParam(value="okMsg", required=false) String okMsg) {
        model.addAttribute("pageTitle", "Mensajes recibidos");
        model.addAttribute("items", repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")));
        if (okMsg != null) model.addAttribute("okMsg", okMsg);
        return "mensajes";
    }

    // --- Eliminar por id ---
    @PostMapping("/mensajes/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            ra.addFlashAttribute("okMsg", "Mensaje #" + id + " eliminado.");
        } else {
            ra.addFlashAttribute("errorMsg", "El mensaje #" + id + " no existe.");
        }
        return "redirect:/mensajes";
    }

    // --- Exportar CSV ---
    @GetMapping(value = "/mensajes/export.csv", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<byte[]> exportCsv() {
        List<Contacto> items = repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        // BOM para Excel + cabecera
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF'); // BOM UTF-8
        sb.append("id,created_at,nombres,apellidos,correo,semestre,descripcion\n");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId tz = ZoneId.systemDefault();

        for (Contacto c : items) {
            String created = c.getCreatedAt() != null
                    ? ZonedDateTime.ofInstant(c.getCreatedAt(), tz).format(fmt)
                    : "";
            sb.append(csv(c.getId()))
              .append(',')
              .append(csv(created))
              .append(',')
              .append(csv(c.getNombres()))
              .append(',')
              .append(csv(c.getApellidos()))
              .append(',')
              .append(csv(c.getCorreo()))
              .append(',')
              .append(csv(c.getSemestre()))
              .append(',')
              .append(csv(c.getDescripcion()))
              .append('\n');
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);

        String filename = "mensajes_" + ZonedDateTime.now(tz).format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")) + ".csv";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    // --- Utilidad para CSV: comillas dobles y nulos ---
    private static String csv(Object o) {
        if (o == null) return "\"\"";
        String s = o.toString();
        // Escape de comillas dobles duplicándolas
        s = s.replace("\"", "\"\"");
        // Envolver siempre en comillas para soportar comas y saltos de línea
        return "\"" + s + "\"";
    }
}
