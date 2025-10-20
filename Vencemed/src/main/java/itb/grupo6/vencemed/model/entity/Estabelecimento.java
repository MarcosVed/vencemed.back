package itb.grupo6.vencemed.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "Estabelecimento")
public class Estabelecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nome;

    @Column(length = 255)
    private String info;

    @Column(length = 9, nullable = false)
    private String cep;

    @Column(length = 10, nullable = false)
    private String numero;

    @Column(length = 40)
    private String complemento;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Column(length = 20, nullable = false)
    private String telefone;

    @Column(length = 20, nullable = false)
    private String tipo;

    @Column(length = 30, nullable = false)
    private String coleta;

    private LocalDateTime dataCadastro;

    @Column(length = 20, nullable = false)
    private String statusEstabelecimento;

    @Column(length = 20, nullable = false)
    private String cnpj;

    @Lob
    @Column(name = "fotoest")
    private byte[] fotoEst;

    @Column(length = 100)
    private String endereço;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getColeta() {
        return coleta;
    }

    public void setColeta(String coleta) {
        this.coleta = coleta;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getStatusEstabelecimento() {
        return statusEstabelecimento;
    }

    public void setStatusEstabelecimento(String statusEstabelecimento) {
        this.statusEstabelecimento = statusEstabelecimento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public byte[] getFotoEst() {
        return fotoEst;
    }

    public void setFotoEst(byte[] fotoEst) {
        this.fotoEst = fotoEst;
    }

    public String getEndereço() {
        return endereço;
    }

    public void setEndereço(String endereço) {
        this.endereço = endereço;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
