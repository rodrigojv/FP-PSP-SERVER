package py.org.fundacionparaguaya.pspserver.network.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "organizations_labels", schema = "ps_network")
public class OrganizationLabelEntity {
    @Id
    @GenericGenerator(
            name = "organizationsLabelsSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = SequenceStyleGenerator.SCHEMA, value = "ps_network"),
                    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "organizations_labels_id_seq"),
                    @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1"),
                    @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
            }
    )
    @GeneratedValue(generator = "organizationsLabelsSequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(targetEntity = OrganizationEntity.class)
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    @ManyToOne(targetEntity = LabelEntity.class)
    @JoinColumn(name = "label_id")
    private LabelEntity label;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
        this.organization = organization;
    }

    public LabelEntity getLabel() {
        return label;
    }

    public void setLabel(LabelEntity label) {
        this.label = label;
    }

    @Transient
    public Optional<OrganizationEntity> getOrganizationOpt() {
        return Optional.ofNullable(this.organization);
    }

    @Transient
    public Optional<LabelEntity> getLabelOpt() {
        return Optional.ofNullable(this.label);
    }

    @Override
    public String toString() {
        return "OrganizationLabelEntity [id=" + id + ", organization=" + organization
                + ", label=" + label + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (id == null || obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrganizationLabelEntity toCompare = (OrganizationLabelEntity) obj;
        return id.equals(toCompare.id);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
